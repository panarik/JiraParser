package com.github.panarik.jiraParser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.panarik.jiraParser.api.GetIssue;
import com.github.panarik.jiraParser.parse.IssueList;
import com.github.panarik.jiraParser.parse.IssuePreview;
import com.github.panarik.jiraParser.parse.Parser;
import com.github.panarik.jiraParser.parse.history.IssueHistory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main implements GetIssue, Parser {

    //Поля запросов по API
    private static String GET = "GET"; //тип запроса
    private static String URL = "https://panariks.atlassian.net";
    private static String urlSearch = "https://panariks.atlassian.net/rest/api/2/search?jql=project=TEST&fields=issue&startAt=0&maxResults=8000";
    private static String URLGETTASK = "https://panariks.atlassian.net/rest/api/2/issue/";
    private static String authToken;

    //JSON объекты и поля
    //Списки тасок
    private static IssueList issuesList; //список тасок
    private static String issuesJSON; //JSON со списком тасок
    private static List<IssuePreview> issuesListPreview; //список полей каждой таски
    //каждая таска (история изменения таски)
    private static String issueHistoryJSON; //JSON с полями истории таски
    private static List<IssueHistory> issueHistory; //массив объектов с полями тасок (история изменения таски)
    /* */

    //DB поля клиента sqlite
    private static Connection connection;
    private static Statement statement;

    public static void main(String[] args) throws IOException, InterruptedException {

        auth();
        searchIssues();
        getIssues();
        getIssueHistory();

//        //отправляет таски в ДБ
//        try {
//            connectDB();
//            putIssuesOnDB();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            disconnect();
//        }

    }

    private static void putIssuesOnDB() {
    }

    private static void disconnect() {
        try {
            if (connection != null) connection.close();
            if (statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void connectDB() throws SQLException {
        //create DB
        connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/jiraIssues.db");
        statement = connection.createStatement();

    }

    private static void auth() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert Your Token>>>>>>");
        authToken = scanner.next();
    }


    private static void getIssues() throws IOException, InterruptedException {
        //формируем URL запроса всех полей каждой таски
        for (IssuePreview issuePreview : issuesListPreview) {
            GetIssue.getIssue((URLGETTASK + issuePreview.getKey()), authToken); //формируем URL запроса таски с KEY каждой таски
            Thread.sleep(100);
            //ToDo делаем запись всех приходящих JSON в объекты
        }
    }

    private static void getIssueHistory() throws IOException, InterruptedException {
        //формируем URL запроса всех полей связанных с изменением каждой таски
        for (int i = 0; i< issuesListPreview.size(); i++) {
            issueHistoryJSON = GetIssue.getIssue((URLGETTASK + issuesListPreview.get(i).getKey()) + "/changelog?startAt=0&maxResults=100", authToken); //формируем URL запроса таски с KEY каждой таски и складываем результат в String
            //парсим на объекты
            issueHistory = new ArrayList<IssueHistory>();
            issueHistory.add(Parser.parseIssueHistory(issueHistoryJSON)); //парсим JSON и выводим поля истории каждой таски
            Thread.sleep(100);
        }
    }

    private static void searchIssues() throws IOException {
        //выводим тело ответа Жиры со списком тасок
        issuesJSON = GetIssue.getIssue(urlSearch, authToken);
        //парсим на объекты
        issuesList = Parser.parseIssueList(issuesJSON); //парсим JSON и выводим поля списка тасок
        issuesListPreview = issuesList.getIssues(); //массив со списком полей (id, key) для всех тасок.
        //дебаг логи
        System.out.println("IssueList fields: " + issuesList.toString());
        System.out.println("IssuePreview fields: " + issuesListPreview.toString());
    }

}
