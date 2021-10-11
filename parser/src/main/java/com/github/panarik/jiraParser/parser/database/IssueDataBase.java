package com.github.panarik.jiraParser.parser.database;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class IssueDataBase {

    private static final String CREATE_HISTORY_TABLE = "create table if not exists history (id integer primary key, key text, authorDisplayName text, created text, field text, fromString text, toString text);";
    private static final String CREATE_SEARCH_TABLE = "create table if not exists search (id integer primary key, jid text, key text);";
    private static final String FILE_PATH = "jdbc:sqlite:jiraIssues.db";
    private Connection connection;
    private Statement statement;

    private static final Logger log = LogManager.getLogger();

    public IssueDataBase() {
        try {
            this.connection = DriverManager.getConnection(FILE_PATH);
            this.statement = connection.createStatement(); //connect to database
        } catch (SQLException e) {
            log.throwing(Level.ERROR, e);
            e.printStackTrace();
        }
    }

    public Statement getStatement() {
        return this.statement;
    }

    public void createSearchTable() {
        try {
            this.statement.execute(CREATE_SEARCH_TABLE); //create table if it's not exist
        } catch (SQLException e) {
            log.throwing(Level.ERROR, e);
            e.printStackTrace();
        }
    }

    public void createHistoryTable() {
        try {
            this.statement.execute(CREATE_HISTORY_TABLE);
        } catch (SQLException e) {
            log.throwing(Level.ERROR, e);
            e.printStackTrace();
        }
    }

    public void insertSearchTable(int id, String jid, String key) {
        try {
            this.statement.executeUpdate("insert into search (id, jid, key) values('" + id + "', '" + jid + "', '" + key + "');");
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