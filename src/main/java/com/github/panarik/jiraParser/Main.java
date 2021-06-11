package com.github.panarik.jiraParser;

import java.io.IOException;
import java.util.Scanner;

public class Main implements GetIssue {

    private static String authToken;
    private static final String issueKey = "TEST-1";

    public static void main(String[] args) throws IOException {

        GetIssue client = new GetIssue() {
            @Override
            public void getIssue(String issueKey, String authToken) throws IOException {
                GetIssue.super.getIssue(issueKey, authToken);
            }
        };

        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert Your Token>>>>>>");
        authToken = scanner.next();

//        client.getIssue(issueKey, authToken);
//        client.getIssueHistory(issueKey, authToken);
        client.getIssues(authToken);





    }



}
