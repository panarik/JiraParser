package com.github.panarik.jiraParser.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public interface GetIssue {


    static String getIssue(String url, String authToken) throws IOException {

        //запрашиваем в Жире информацию
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Authorization", "Basic " + authToken)
                .build();

        Response response = client.newCall(request).execute();
        String responseBodyJSON = response.body().string(); //выводим тело ответа

        //выводим статус
        System.out.println("\n" + request);
        System.out.println(response);
        System.out.println("JSON response body:");
        System.out.println(responseBodyJSON);


        return responseBodyJSON;
    }

}
