package com.github.panarik.jiraParser.parser.parse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.panarik.jiraParser.parser.parse.history.IssueHistory;
import com.github.panarik.jiraParser.parser.parse.search.IssueList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT;

public class ParseJSON {
    private static final Logger log = LogManager.getLogger();
    private static IssueList issuesList; //список тасок

    public static IssueList issueList(String jSON) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            issuesList = mapper.readValue(jSON, IssueList.class); //парсим список тасок из JSON
        } catch (JsonProcessingException e) {
            log.throwing(Level.ERROR, e);
            e.printStackTrace();
        }

        log.trace("IssueList fields: {}", issuesList.toString());
        return issuesList;
    }

    public static IssueHistory issueHistory(String jSON) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        IssueHistory issueHistory = mapper.readValue(jSON, IssueHistory.class); //список тасок
        log.trace("IssueHistory fields: {}", issueHistory.toString());
        return issueHistory;
    }


}
