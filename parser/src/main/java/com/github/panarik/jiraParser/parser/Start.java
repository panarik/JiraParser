package com.github.panarik.jiraParser.parser;

import com.github.panarik.jiraParser.parser.util.Parser;
import com.github.panarik.jiraParser.parser.util.Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Start {
    private static ExecutorService manager; //manage threads

    public static void main(String[] args) {
        manager = Executors.newCachedThreadPool();
        manager.execute(Server::run); //запуск сервера
        manager.execute(Parser::run); //запуск парсера Jira
        manager.shutdown();
    }
}
