package com.github.panarik.jiraParser.parser.http;

import com.github.panarik.jiraParser.parser.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GetIssue {

    private static Response response;
    private static Request request;
    private static String responseBodyJSON;

    private static final Logger log = LogManager.getLogger();

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
            log.throwing(Level.ERROR, e);
            e.printStackTrace();
        }
        //проверяем на 401 - Unauthorized
        if (response.code() == 401) {
            String message = "RESPONSE CODE IS 403 - UNAUTHORIZED! Please update Your token. Filename: \"token\", file path: \\resources. Put Your token into file";
            log.error(message);
        }
        log.trace("Request: {}", request);
        log.trace("Response: {}", response);
        log.trace("JSON response body: {}", responseBodyJSON);
        return responseBodyJSON;
    }

}
