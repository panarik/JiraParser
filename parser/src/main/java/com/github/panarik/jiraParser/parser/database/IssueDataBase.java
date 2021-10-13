package com.github.panarik.jiraParser.parser.database;

import com.sun.jdi.connect.Connector;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void insertTable(String table, int id, String jid, String key) {
        try {
            this.statement.executeUpdate("insert into search (id, jid, key) values(" + id + ", '" + jid + "', '" + key + "');");
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
}