package com.github.panarik.jiraParser.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.panarik.jiraParser.parser.http.GetIssue;
import com.github.panarik.jiraParser.parser.parse.ParseJSON;
import com.github.panarik.jiraParser.parser.parse.search.IssueList;
import com.github.panarik.jiraParser.parser.parse.search.IssuePreview;
import com.github.panarik.jiraParser.parser.parse.history.IssueHistory;
import com.github.panarik.jiraParser.parser.util.Config;
import com.github.panarik.jiraParser.parser.util.Log;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {

    //логгер
    private static final Logger log = LogManager.getLogger();

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

    public static void run() {

//        auth(); //ввод токена с консоли
        authFromFile(); //ввод токена из файла
        searchIssues(); //получаем из API Jira все таски

        getIssueHistory(); //получаем из API Jira все поля истории каждой таски
        putIssuesOnDatabase(); //создаём БД со всеми тасками
        putIssueHistoryOnDatabase(); //создаём БД со всеми полями истории каждой таски
    }

    private static void authFromFile() {
        try (FileInputStream fis = new FileInputStream(Config.JIRA_TOKEN)) {
            int x; //складываем по символам токен в виде байтов
            StringBuilder s = new StringBuilder(); //строка с токеном
            while ((x = fis.read()) > -1) {
                s.append((char) x);
            }
            authToken = s.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void putIssueHistoryOnDatabase() {

        int id = 0; //устанавливаем начальное id для таблицы с историей тасок

        //отправляет список тасок в БД (таблица search)
        try {
            connectIssueHistoryDB(); //коннектимся или создаём БД
            //ToDo вместо очистки таблицы можно архивировать файлик с предыдущим запросом и создавать сегодняшнюю БД
            clearDB("history"); //очищаем все строки в таблице "search"

            /*ToDo
             * складываем в таблицу с тасками (история изменения) все найденное подряд
             */

            for (int i = 0; i < issueHistory.size(); i++) {

                //получаем и обрезаем KEY текущей таски
                String thisKeyAll = issueHistory.get(i).getSelf();
                StringBuilder thisKeyCut = new StringBuilder(thisKeyAll);
                thisKeyCut.delete(0, 48); //удаляем URL до значения KEY
                int thisKeySum = thisKeyCut.length();
                thisKeyCut.delete(thisKeySum - 35, thisKeySum); //удаляем параметры после значения KEY
                String thisKey = thisKeyCut.toString();

                //выясняем сколько элементов истории (value полей) есть у каждой таски
                int thisValues = issueHistory.get(i).getValues().size();
                //вытаскиваем все нужные поля из каждого value поля таски
                for (int j = 0; j < thisValues; j++) {

                    String thisValueAuthor = issueHistory.get(i).getValues().get(j).getAuthor().getDisplayName(); //получаем пользователя
                    String thisValueCreated = issueHistory.get(i).getValues().get(j).getCreated(); //получаем дату изменения поля таски

                    //извлекаем измененные пользователем поля
                    int valueItems = issueHistory.get(i).getValues().get(j).getItems().size(); //узнаем сколько полей пользователь изменил
                    for (int k = 0; k < valueItems; k++) {
                        String thisValueField = issueHistory.get(i).getValues().get(j).getItems().get(k).getField(); //получаем тип меняемого пользователем поля таски
                        String thisValueFieldFrom = issueHistory.get(i).getValues().get(j).getItems().get(k).getFromString(); //получаем исходное состояние поля таски
                        String thisValueFieldTo = issueHistory.get(i).getValues().get(j).getItems().get(k).getToString(); //получаем конечное состояние поля таски

                        log.trace("Task History: Key:{}, Author:{}, Created:{}, Field:{}, From:{}, To:{}", thisKey, thisValueAuthor, thisValueCreated, thisValueField, thisValueFieldFrom, thisValueFieldTo);
                        //заполняем полученные данные в табличку
                        id++;
                        statement.executeUpdate("insert into history" +
                                "(id, key, authorDisplayName, created, field, fromString, toString) " +
                                "values(" +
                                "'" + id + "', " +
                                "'" + thisKey + "', " +
                                "'" + thisValueAuthor + "', " +
                                "'" + thisValueCreated + "', " +
                                "'" + thisValueField + "', " +
                                "'" + thisValueFieldFrom + "', " +
                                "'" + thisValueFieldTo + "'" +
                                ");");
                    }
                }
            }
        } catch (SQLException e) {
            log.throwing(Level.ERROR, e);
        } finally {
            disconnect();
        }
        log.info("PARSER - work has complete, add {} tasks to database", issuesList.getIssues().size());

    }

    private static void connectIssueHistoryDB() throws SQLException {
        //create DB with
        connection = DriverManager.getConnection("jdbc:sqlite:jiraIssues.db");
        statement = connection.createStatement();
        //create table if its not exist
        statement.execute("create table if not exists history (" +
                "id integer primary key," +
                "key text, " +
                "authorDisplayName text, " +
                "created text, " +
                "field text, " +
                "fromString text, " +
                "toString text);"); //таблица с историей изменения каждой таски
    }

    private static void putIssuesOnDatabase() {
        //отправляет список тасок в БД (таблица search)
        try {
            connectIssuesDB(); //коннектимся или создаём БД
            clearDB("search"); //очищаем все строки в таблице "search"
            //проходим по списку всех тасок, выдергиваем из них поля (id, key) и помещаем в таблицу с тасками
            for (int i = 0; i < issuesListPreview.size(); i++) {
                putIssuesIntoSearchTable(
                        i + 1, //id в таблице
                        issuesList.getIssues().get(i).getId(), //id таски в Жире
                        issuesList.getIssues().get(i).getKey()); //наименование таски в Жире
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private static void clearDB(String tableName) throws SQLException {
        statement.executeUpdate("delete from " + tableName + ";");
    }

    private static void putIssuesIntoSearchTable(int id, String jid, String key) throws SQLException {
        statement.executeUpdate("insert into search (id, jid, key) values('" + id + "', '" + jid + "', '" + key + "');");
    }

    private static void disconnect() {
        try {
            if (connection != null) connection.close();
            if (statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void connectIssuesDB() throws SQLException {
        //create DB with
        connection = DriverManager.getConnection("jdbc:sqlite:jiraIssues.db");
        statement = connection.createStatement();
        //create table if its not exist
        statement.execute("create table if not exists search (id integer primary key, jid text, key text);"); //таблица со списком всех тасок
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

    private static void getIssueHistory() {
        //формируем URL запроса всех полей связанных с изменением каждой таски
        issueHistory = new ArrayList<>();
        try {
            for (IssuePreview issuePreview : issuesListPreview) {
                issueHistoryJSON = GetIssue.getIssue((URLGETTASK + issuePreview.getKey()) + "/changelog?startAt=0&maxResults=100", authToken); //формируем URL запроса таски с KEY каждой таски и складываем результат в String
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
        issuesJSON = GetIssue.getIssue(urlSearch, authToken);
        //парсим на объекты
        issuesList = ParseJSON.issueList(issuesJSON); //парсим JSON и выводим поля списка тасок
        issuesListPreview = issuesList.getIssues(); //массив со списком полей (id, key) для всех тасок.
        log.trace("IssueList fields: {}", issuesList.toString());
        log.trace("IssuePreview fields: {}", issuesListPreview.toString());
    }

}
