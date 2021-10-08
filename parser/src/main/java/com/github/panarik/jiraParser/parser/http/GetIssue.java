package com.github.panarik.jiraParser.parser.http;

import com.github.panarik.jiraParser.parser.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class GetIssue {

    private static Response response;
    private static Request request;
    private static String responseBodyJSON;

    public static String getIssue(String url, String authToken) {
        //запрашиваем в Jira информацию
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Authorization", "Basic " + authToken)
                .build();
        try {
            response = client.newCall(request).execute();
            responseBodyJSON = response.body().string(); //выводим тело ответа
        } catch (IOException e) {
            Log.debug(e);
            e.printStackTrace();
        }

        //проверяем на 401 - Unauthorized
        if (response.code() == 401) {
            String message = "RESPONSE CODE IS 403 - UNAUTHORIZED! Please update Your token. Filename: \"token\", file path: \\resources. Put Your token into file";
            Log.debug(message);
        }

        //выводим статус
        System.out.println("Request: " + request);
        Log.debug("Request:" + request);
        System.out.println("Response: " + response);
        Log.debug("Response:" + response);
        System.out.println("\nJSON response body:" + responseBodyJSON);
        return responseBodyJSON;
    }

}
