package com.github.panarik.jiraParser.parser.http;

import com.github.panarik.jiraParser.parser.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public interface GetIssue {


    static String getIssue(String url, String authToken) throws IOException {

        //запрашиваем в Jira информацию
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
        System.out.println("Request: " + request);
        Log.debug("Request:" + request);
        System.out.println("Response: " + response);
        Log.debug("Response:" + response);
        System.out.println("\nJSON response body:" + responseBodyJSON);

        return responseBodyJSON;
    }

}
