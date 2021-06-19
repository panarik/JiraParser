package com.github.panarik.jiraParser.parse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface Parser {

    static IssueList parseIssueList (String jSON) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IssueList issuesList = mapper.readValue(jSON, IssueList.class); //список тасок

        System.out.println("IssueList fields: " + issuesList.toString());
        return issuesList;
    }


}
