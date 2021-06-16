package com.github.panarik.jiraParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.panarik.jiraParser.api.GetIssue;
import com.github.panarik.jiraParser.parse.IssueList;
import com.github.panarik.jiraParser.parse.IssuePreview;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

public class Main implements GetIssue {

    private static String GET = "GET"; //тип запроса
    private static String URL = "https://panariks.atlassian.net";
    private static String urlSearch = "https://panariks.atlassian.net/rest/api/2/search?jql=project=TEST&fields=issue&startAt=0&maxResults=8000";
    private static String URLGETTASK = "https://panariks.atlassian.net/rest/api/2/issue/";
    private static String authToken;
    private static final String issueKey = "TEST-1";

    //JSON объекты и поля
    //Списки тасок
    private static IssueList issuesList; //список тасок
    private static String issuesJSON; //JSON со списком тасок
    private static List<IssuePreview> issuesListPreview; //список полей каждой таски
    //каждая таска
    /* */

    //DB поля клиента sqlite
    private static Connection connection;
    private static Statement statement;

    public static void main(String[] args) throws IOException, InterruptedException {

        auth();
        searchIssues();
        getIssues();

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


    public static void getIssues() throws IOException, InterruptedException {
        //формируем URL для запроса каждой таски
        for (int i = 0; i < issuesListPreview.size(); i++) {
            GetIssue.getIssue((URLGETTASK + issuesListPreview.get(i).getKey()), authToken); //формируем URL запроса таски с KEY каждой таски
            Thread.sleep(100);
        }
    }

    public void getIssueHistory(String issueKey) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(URL + "/rest/api/2/issue/" + issueKey + "/changelog?startAt=0&maxResults=100")
                .method(GET, null)
                .addHeader("Authorization", "Basic " + authToken)
                .build();
        System.out.println(request);
        Response response = client.newCall(request).execute();
        System.out.println(response);
        System.out.println(response.body().string());
    }

    public static void searchIssues() throws IOException {
        //выводим тело ответа со списком тасок
        issuesJSON = GetIssue.getIssue(urlSearch, authToken);
        //парсим на объекты
        ObjectMapper mapper = new ObjectMapper();
        issuesList = mapper.readValue(issuesJSON, IssueList.class); //список тасок
        issuesListPreview = issuesList.getIssues(); //массив со списком полей (id, key) для всех тасок

        System.out.println("IssueList fields: " + issuesList.toString());
        System.out.println("IssuePreview fields: " + issuesListPreview.toString());
    }


}
