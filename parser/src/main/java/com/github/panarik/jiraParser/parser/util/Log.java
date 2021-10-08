package com.github.panarik.jiraParser.parser.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Log {

    //файл
    private final static String LOG_PATH = "resources/";
    private final static String LOG_NAME = "log-";
    //даты в файле
    private static Calendar calendar; //текущая дата

    public static void debug(String log) {
        //готовим данные
        byte[] buffer = (lineDate()+log).getBytes(StandardCharsets.UTF_8);
        //записываем данные в файл
        try (OutputStream out = new FileOutputStream(LOG_PATH+LOG_NAME+fileDate(), true)) {
            out.write(buffer);
            out.write("\n".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void debug(Exception exception) {
        StackTraceElement[] elements = exception.getStackTrace();
        try (OutputStream out = new FileOutputStream(LOG_PATH+LOG_NAME+fileDate(), true)) {
            //готовим данные
            ArrayList<String> exceptionList = new ArrayList<>();
            exceptionList.add("EXCEPTION!!!");
            exceptionList.add(exception.getMessage());
            for (StackTraceElement e : elements) {
                exceptionList.add(e.toString());
            }
            //записываем всё в файл
            for (String line : exceptionList) {
                byte[] buffer = (lineDate()+line+"\n").getBytes(StandardCharsets.UTF_8);
                out.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String lineDate() {
        //определяем дату
        DateFormat formatLine = new SimpleDateFormat("HHmmss");
        calendar = new GregorianCalendar();
        //дата для строки
        String line = formatLine.format(calendar.getTime()) + ": ";
        return line;
    }

    private static String fileDate() {
        //определяем дату
        DateFormat formatFile = new SimpleDateFormat("yyMMdd");
        calendar = new GregorianCalendar();
        //дата для файла
        String date = formatFile.format(calendar.getTime());
        return date;
    }
}


