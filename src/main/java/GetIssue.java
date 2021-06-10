

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import parse.IssueList;

import java.io.DataInput;
import java.io.IOException;

public interface GetIssue {

    String GET = "GET";
    String URL = "https://panariks.atlassian.net";
    String ISSUEPATH = "/rest/api/2/issue/";

    default void getIssue(String issueKey, String authToken) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(URL+ISSUEPATH+issueKey)
                .method(GET, null)
                .addHeader("Authorization", "Basic "+authToken)
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
                .url(URL+ISSUEPATH+issueKey+"/changelog?startAt=0&maxResults=100")
                .method(GET, null)
                .addHeader("Authorization", "Basic "+authToken)
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
                .addHeader("Authorization", "Basic "+authToken)
                .build();
        Response response = client.newCall(request).execute();

        //выводим статус
        System.out.println("\n"+request);
        System.out.println(response);
        System.out.println(response.body().string());

        //парсим на объекты
        ObjectMapper mapper = new ObjectMapper();
        String issuesInString = response.body().toString();

        //ToDo дебажим парсинг (эталон JSONа в файлике тут: src/main/resources/exampleIssueList.json)
        IssueList issueList = mapper.readValue(issuesInString, IssueList.class);
    }



}
