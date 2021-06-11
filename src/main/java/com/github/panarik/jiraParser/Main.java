package com.github.panarik.jiraParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.panarik.jiraParser.parse.IssueList;
import com.github.panarik.jiraParser.parse.IssuePreview;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static String GET = "GET"; //тип запроса
    private static String URL = "https://panariks.atlassian.net";
    private static String ISSUEPATH = "/rest/api/2/issue/";
    private static String authToken;
    private static final String issueKey = "TEST-1";

    //JSON объекты и поля
    private static IssueList issueList; //список тасок
    private static String issuesJSON; //JSON со списком тасок
    private static List<IssuePreview> issues; //список полей каждой таски

    public static void main(String[] args) throws IOException {

        auth();
        searchIssues();
        getIssues();
    }

    private static void auth() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert Your Token>>>>>>");
        authToken = scanner.next();
    }


    public static void getIssues() throws IOException {

        issues = issueList.getIssues(); //получаем список тасок


        for (int i = 0; i < issues.size(); i++) {

            System.out.println(issues.get(i).toString());

        }
//
//        //запускаем запрос
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        Request request = new Request.Builder()
//                .url(URL + ISSUEPATH + "TEST-1")
//                .method(GET, null)
//                .addHeader("Authorization", "Basic " + authToken)
//                .build();
//        System.out.println(request);
//        Response response = client.newCall(request).execute();
//        System.out.println(response);
//        System.out.println(response.body().string());
    }

    public void getIssueHistory(String issueKey) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(URL + ISSUEPATH + issueKey + "/changelog?startAt=0&maxResults=100")
                .method(GET, null)
                .addHeader("Authorization", "Basic " + authToken)
                .build();
        System.out.println(request);
        Response response = client.newCall(request).execute();
        System.out.println(response);
        System.out.println(response.body().string());
    }

    public static void searchIssues() throws IOException {

        //запрашиваем таски
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://panariks.atlassian.net/rest/api/2/search?jql=project=TEST&fields=issue&startAt=0&maxResults=8000")
                .method(GET, null)
                .addHeader("Authorization", "Basic " + authToken)
                .build();
        Response response = client.newCall(request).execute();
        issuesJSON = response.body().string();

        //выводим статус
        System.out.println("\n" + request);
        System.out.println(response);
        System.out.println("Тело JSON:");
        System.out.println(issuesJSON);

        //парсим на объекты
        ObjectMapper mapper = new ObjectMapper();
        issueList = mapper.readValue(issuesJSON, IssueList.class);
        System.out.println("Поля объекта IssueList: " + issueList.toString());
        System.out.println("Поля объектов IssuePreview: " + issueList.getIssues().toString());


    }


}
