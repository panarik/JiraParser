package com.github.panarik.jiraParser.parser.util;

import com.github.panarik.jiraParser.parser.Parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Start {

    private static final Object MONITOR = new Object();
    private static String letter = " ";
    private static ExecutorService manager; //manage threads

    public static void main(String[] args) {

        //домашка п.2
        manager = Executors.newCachedThreadPool();
        manager.execute(Server::run); //запуск сервера
        manager.execute(Parser::run); //запуск парсера Jira
    }
}
