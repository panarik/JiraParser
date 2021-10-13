package com.github.panarik.jiraParser.parser.util;

import com.github.panarik.jiraParser.parser.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Start {
    private static ExecutorService manager; //manage threads

    public static void main(String[] args) {
        //path to config file log4j2
        System.out.println(System.setProperty("Dlog4j.configurationFile", "file:/resources/config/log4j2config.xml"));

        manager = Executors.newCachedThreadPool();
        manager.execute(Server::run); //запуск сервера
        manager.execute(Parser::run); //запуск парсера Jira
        manager.shutdown();

    }
}
