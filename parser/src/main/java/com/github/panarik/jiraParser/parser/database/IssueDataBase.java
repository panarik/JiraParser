package com.github.panarik.jiraParser.parser.database;

import com.github.panarik.jiraParser.parser.parse.history.IssueHistory;
import com.github.panarik.jiraParser.parser.parse.search.IssueList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssueDataBase {

    private static final String CREATE_HISTORY_TABLE = "create table if not exists history (id integer primary key, key text, authorDisplayName text, created text, field text, fromString text, toString text);";
    private static final String CREATE_SEARCH_TABLE = "create table if not exists search (id integer primary key, jid text, key text);";
    private static final String BASE_NAME = "jdbc:sqlite:jiraIssues.db";

    private Connection connection;
    private Statement statement;

    private static final Logger log = LogManager.getLogger();

    //connect database
    public IssueDataBase() {
        try {
            this.connection = DriverManager.getConnection(BASE_NAME);
            this.statement = connection.createStatement(); //connect to database
            //create tables
            this.statement.execute(CREATE_SEARCH_TABLE);
            this.statement.execute(CREATE_HISTORY_TABLE);
            //clear tables
            statement.executeUpdate("delete from search;");
            statement.executeUpdate("delete from history;");
        } catch (SQLException e) {
            log.throwing(Level.ERROR, e);
            e.printStackTrace();
        }
    }

    public void putHistory(List<IssueHistory> issueHistory) {
        Map<String, String> items = new HashMap();

        Integer id = 0;
        for (int i = 0; i < issueHistory.size(); i++) {
            items.put("key", cutKey(issueHistory.get(i).getSelf())); //KEY текущей таски
            int thisValues = issueHistory.get(i).getValues().size(); //количество элементов истории (value полей) в каждой таске

            //вытаскиваем все нужные поля из каждого value поля таски
            for (int j = 0; j < thisValues; j++) {
                items.put("authorDisplayName", issueHistory.get(i).getValues().get(j).getAuthor().getDisplayName()); //имя пользователя
                items.put("created", issueHistory.get(i).getValues().get(j).getCreated()); //дата изменения поля таски

                //извлекаем измененные пользователем поля
                int valueItems = issueHistory.get(i).getValues().get(j).getItems().size(); //узнаем сколько полей пользователь изменил
                for (int k = 0; k < valueItems; k++) {
                    items.put("field", issueHistory.get(i).getValues().get(j).getItems().get(k).getField()); //тип меняемого пользователем поля таски
                    items.put("fromString", issueHistory.get(i).getValues().get(j).getItems().get(k).getFromString()); //исходное состояние поля таски
                    items.put("toString", issueHistory.get(i).getValues().get(j).getItems().get(k).getToString()); //конечное состояние поля таски
                    id++;
                    items.put("id", id.toString());
                    log.trace("Task History: key:{}, authorDisplayName:{}, created:{}, field:{}, fromString:{}, toString:{}", items.get("key"), items.get("authorDisplayName"), items.get("created"), items.get("field"), items.get("fromString"), items.get("toString"));
                    //заполняем полученные данные в табличку
                    insertTable("history", items);
                }
            }
        }
        log.info("added {} items to history table", items.size());
    }

    public void putSearch(IssueList issuesList)  {
        Map<String, String> items = new HashMap();
        for (int i = 0; i < issuesList.getIssues().size(); i++) {
            Integer id = i+1;
            items.put("id", id.toString());
            items.put("jid", issuesList.getIssues().get(i).getId()); //id таски в Jira
            items.put("key", issuesList.getIssues().get(i).getKey()); //наименование таски в Jira
            insertTable("search",items);
        }
    }

    public void insertTable(String table, Map queryItems) {
        try {
            switch (table) {
                case ("search") -> this.statement.executeUpdate("insert into search (id, jid, key) values(" + queryItems.get("id") + ", '" + queryItems.get("jid") + "', '" + queryItems.get("key") + "');");
                case ("history") -> this.statement.execute("insert into history (id, key, authorDisplayName, created, field, fromString, toString) values(" +
                        "'" + queryItems.get("id") + "', " +
                        "'" + queryItems.get("key") + "', " +
                        "'" + queryItems.get("authorDisplayName") + "', " +
                        "'" + queryItems.get("created") + "', " +
                        "'" + queryItems.get("field") + "', " +
                        "'" + queryItems.get("fromString") + "', " +
                        "'" + queryItems.get("toString") + "'" +
                        ");");
            }
        } catch (SQLException e) {
            log.throwing(Level.ERROR, e);
            e.printStackTrace();
        }
    }

    public void insertTable(String table, int id, String jiraId, String key) {
        try {
            this.statement.executeUpdate("insert into " + table + " (id, jid, key) values(" + id + ", '" + jiraId + "', '" + key + "');");
        } catch (SQLException e) {
            log.throwing(Level.ERROR, e);
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {
            if (this.connection != null) connection.close();
            if (this.statement != null) statement.close();
        } catch (SQLException e) {
            log.throwing(Level.ERROR, e);
            e.printStackTrace();
        }
    }

    private String cutKey(String string) {
        log.trace("String with KEY: {}", string);
        StringBuilder builder = new StringBuilder(string);
        builder.delete(0, 48); //удаляем URL до значения KEY
        int length = builder.length();
        builder.delete(length - 35, length); //удаляем параметры после значения KEY
        log.trace("String with KEY: {}", builder.toString());
        return builder.toString();
    }
}