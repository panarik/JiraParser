package com.github.panarik.jiraParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.panarik.jiraParser.parse.IssueList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public interface GetIssue {

    String GET = "GET";
    String URL = "https://panariks.atlassian.net";
    String ISSUEPATH = "/rest/api/2/issue/";

    default void getIssue(String issueKey, String authToken) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(URL + ISSUEPATH + issueKey)
                .method(GET, null)
                .addHeader("Authorization", "Basic " + authToken)
                .build();
        System.out.println(request);
        Response response = client.newCall(request).execute();
        System.out.println(response);
        System.out.println(response.body().string());
    }

    default void getIssueHistory(String issueKey, String authToken) throws IOException {
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

    default void getIssues(String authToken) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://panariks.atlassian.net/rest/api/2/search?jql=project=TEST&fields=issue&startAt=0&maxResults=8000")
                .method(GET, null)
                .addHeader("Authorization", "Basic " + authToken)
                .build();
        Response response = client.newCall(request).execute();
        String issuesInString = response.body().string();

        //выводим статус
        System.out.println("\n" + request);
        System.out.println(response);
        System.out.println("Тело JSON:");
        System.out.println(issuesInString);

        //парсим на объекты
        ObjectMapper mapper = new ObjectMapper();
        IssueList issueList = mapper.readValue(issuesInString, IssueList.class);
        System.out.println("Поля объекта IssueList: "+issueList.toString());
        System.out.println("Поля объектов IssuePreview: "+issueList.getIssues().toString());



    }


}
