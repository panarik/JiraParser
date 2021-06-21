package com.github.panarik.jiraParser.parse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.panarik.jiraParser.parse.history.IssueHistory;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT;

public interface Parser {

    static IssueList parseIssueList (String jSON) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IssueList issuesList = mapper.readValue(jSON, IssueList.class); //список тасок
        //дебаг логи
        System.out.println("IssueList fields: " + issuesList.toString());
        return issuesList;
    }

    static IssueHistory parseIssueHistory (String jSON) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        IssueHistory issueHistory = mapper.readValue(jSON, IssueHistory.class); //список тасок
        //дебаг логи
        System.out.println("\nIssueHistory fields: " + issueHistory.toString());
        return issueHistory;
    }


}
