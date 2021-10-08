package com.github.panarik.jiraParser.parser.parse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.panarik.jiraParser.parser.parse.history.IssueHistory;
import com.github.panarik.jiraParser.parser.parse.search.IssueList;
import okhttp3.Response;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT;

public class ParseJSON {

    public static IssueList issueList(String jSON) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IssueList issuesList = mapper.readValue(jSON, IssueList.class); //список тасок
        //дебаг логи
        System.out.println("IssueList fields: " + issuesList.toString());
        return issuesList;
    }

    public static IssueHistory issueHistory(String jSON) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        IssueHistory issueHistory = mapper.readValue(jSON, IssueHistory.class); //список тасок
        //дебаг логи
        System.out.println("\nIssueHistory fields: " + issueHistory.toString());
        return issueHistory;
    }


}
