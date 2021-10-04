package com.github.panarik.jiraParser.parser.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Log {

    public static void main(String[] args) {
        debug("hello");
    }

    private static String date;
    private final static String LOG_PATH = "resources/log-";

    public static void debug(String log) {
        //определяем дату
        DateFormat formatFile = new SimpleDateFormat("yyMMdd");
        DateFormat formatLine = new SimpleDateFormat("HHmmss");
        Calendar calendar = new GregorianCalendar();
        date = formatFile.format(calendar.getTime());
        String line = formatLine.format(calendar.getTime())+": ";
        System.out.println(date);

        //создаём файл
        File logFile = new File(LOG_PATH+date);
        System.out.println(logFile.exists());
        //готовим данные
        byte[] buffer = (line+log).getBytes(StandardCharsets.UTF_8);
        //записываем данные в файл
        try (OutputStream out = new FileOutputStream(LOG_PATH+date, true)) {
            out.write(buffer);
            out.write("\n".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


