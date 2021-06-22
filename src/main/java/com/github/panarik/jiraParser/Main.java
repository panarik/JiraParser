package com.github.panarik.jiraParser;

import com.github.panarik.jiraParser.api.GetIssue;
import com.github.panarik.jiraParser.parse.search.IssueList;
import com.github.panarik.jiraParser.parse.search.IssuePreview;
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
    private static String issueHistoryJSON; //JSON с полями таски (история изменения таски)
    private static List<IssueHistory> issueHistory; //массив объектов с полями тасок (история изменения таски)

    //DB поля клиента sqlite
    private static Connection connection;
    private static Statement statement;

    public static void main(String[] args) throws IOException, InterruptedException {

        auth();
        searchIssues();
        //getIssues();
        getIssueHistory();

        //отправляет список тасок в БД (таблица search)
        try {
            connectDB(); //коннектимся или создаём БД
            clearDB("search"); //очищаем все строки в таблице "search"
            //проходим по списку всех тасок, выдергиваем из них поля (id, key) и помещаем в таблицу с тасками
            for (int i = 0; i < issuesListPreview.size(); i++) {
                putIssuesList("search", issuesList.getIssues().get(i).getId(), issuesList.getIssues().get(i).getKey());
            }
            /*ToDo
             * проходим по каждой таске, (история изменений)
             * выдергиваем из неё все интересные поля, если field=status,
             * складываем в таблицу с тасками (история изменения) все найденное подряд
             */
            for (int i =0; i<issueHistory.size();i++) {

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }

    }

    private static void clearDB(String tableName) throws SQLException {
        statement.executeUpdate("delete from "+tableName+";");
    }

    private static void putIssuesList(String tableName, String jid, String key) throws SQLException {
        statement.executeUpdate("insert into "+tableName+" (jid, key) values('" + jid + "', '" + key + "');");
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
        //create table if its not exist
        statement.execute("create table if not exists search (id integer primary key autoincrement, jid text, key text);"); //таблица со списком всех тасок
        statement.execute("create table if not exists issueHistory (id integer primary key autoincrement, self text, authorDisplayName text, created text, field text, fromString text, toString text);"); //таблица с историей изменения каждой таски


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
        for (int i = 0; i < issuesListPreview.size(); i++) {
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
