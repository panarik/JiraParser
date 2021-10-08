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
        manager.execute(Start::task1); //домашка п.1
        manager.execute(Server::run); //запуск сервера
        manager.execute(Parser::run); //запуск парсера Jira
    }

    private static void task1() {
        for (int i = 0; i<5; i++) {
            new Thread(() -> printA()).start();
            new Thread(() -> printB()).start();
            new Thread(() -> printC()).start();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.debug(e.getMessage());
            }
        }
    }

    private static synchronized void printA() {
        try {
            if (letter.equals(" ") | letter.equals("C")) {
                letter = "A";
                System.out.println(letter);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static synchronized void printB() {
        try {
            if (letter.equals("A")) {
                letter = "B";
                System.out.println(letter);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static synchronized void printC() {
        try {
            if (letter.equals("B")) {
                letter = "C";
                System.out.println(letter);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
