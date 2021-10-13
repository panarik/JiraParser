package com.github.panarik.jiraParser.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.panarik.jiraParser.parser.database.IssueDataBase;
import com.github.panarik.jiraParser.parser.http.GetIssue;
import com.github.panarik.jiraParser.parser.parse.ParseJSON;
import com.github.panarik.jiraParser.parser.parse.search.IssueList;
import com.github.panarik.jiraParser.parser.parse.search.IssuePreview;
import com.github.panarik.jiraParser.parser.parse.history.IssueHistory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    //логгер
    private static final Logger log = LogManager.getLogger();

    //Поля запросов по API
    private static String GET = "GET"; //тип запроса
    private static String URL = "https://panariks.atlassian.net";
    private static String urlSearch = "https://panariks.atlassian.net/rest/api/2/search?jql=project=TEST&fields=issue&startAt=0&maxResults=8000";
    private static String URLGETTASK = "https://panariks.atlassian.net/rest/api/2/issue/";
    //поля авторизации
    private static String tokenAuth;
    private static final String TOKEN_PATH = "resources/token";

    //JSON объекты и поля
    //Списки тасок
    private static IssueList issuesList; //список тасок
    private static String issuesJSON; //JSON со списком тасок
    private static List<IssuePreview> issuesListPreview; //список полей каждой таски
    //каждая таска (история изменения таски)
    private static String issueHistoryJSON; //JSON с полями таски (история изменения таски)
    private static List<IssueHistory> issueHistory; //массив объектов с полями тасок (история изменения таски)

    private static IssueDataBase issueBase;
    private static final String BASE_PATH = "jiraIssues.db";

    public static void run() {
        authFromFile(); //ввод токена из файла
        searchIssues(); //получаем из API Jira все таски
        getIssueHistory(); //получаем из API Jira все поля истории каждой таски

        issueBase = new IssueDataBase(); //create clear database with tables

        //проходим по списку всех тасок, выдергиваем из них поля (id, key) и помещаем в таблицу с тасками
        for (int i = 0; i < issuesListPreview.size(); i++) {
            String id = issuesList.getIssues().get(i).getId(); //id таски в Жире
            String key = issuesList.getIssues().get(i).getKey(); //наименование таски в Жире
            issueBase.insertTable("search",i + 1, id, key);
        }

        issueBase.putHistory(issueHistory);
    }

    private static void authFromFile() {
        try (FileInputStream fis = new FileInputStream(TOKEN_PATH)) {
            int x; //складываем по символам токен в виде байтов
            StringBuilder s = new StringBuilder(); //строка с токеном
            while ((x = fis.read()) > -1) {
                s.append((char) x);
            }
            tokenAuth = s.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static void getIssues() throws IOException, InterruptedException {
        //формируем URL запроса всех полей каждой таски
        for (IssuePreview issuePreview : issuesListPreview) {
            GetIssue.getIssue((URLGETTASK + issuePreview.getKey()), tokenAuth); //формируем URL запроса таски с KEY каждой таски
            Thread.sleep(100);
        }
    }

    private static void getIssueHistory() {
        //формируем URL запрос всех полей связанных с изменением каждой таски
        issueHistory = new ArrayList<>();
        try {
            for (IssuePreview issuePreview : issuesListPreview) {
                issueHistoryJSON = GetIssue.getIssue((URLGETTASK + issuePreview.getKey()) + "/changelog?startAt=0&maxResults=100", tokenAuth); //формируем URL запроса таски с KEY каждой таски и складываем результат в String
                //парсим на объекты
                issueHistory.add(ParseJSON.issueHistory(issueHistoryJSON)); //парсим JSON и выводим поля истории каждой таски
                Thread.sleep(100);
            }
        } catch (InterruptedException | JsonProcessingException e) {
            log.throwing(Level.ERROR, e);
            e.printStackTrace();
        }
    }

    private static void searchIssues() {
        //выводим тело ответа Жиры со списком тасок
        issuesJSON = GetIssue.getIssue(urlSearch, tokenAuth);
        //парсим на объекты
        issuesList = ParseJSON.issueList(issuesJSON); //парсим JSON и выводим поля списка тасок
        issuesListPreview = issuesList.getIssues(); //массив со списком полей (id, key) для всех тасок.
        log.trace("IssueList fields: {}", issuesList.toString());
        log.trace("IssuePreview fields: {}", issuesListPreview.toString());
    }
}
