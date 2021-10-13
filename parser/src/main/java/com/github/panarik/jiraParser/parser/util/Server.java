package com.github.panarik.jiraParser.parser.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    //Server configs
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static final int PORT = 8090;
    private static DataInputStream in;
    private static DataOutputStream out;

    //logging
    private static final Logger log = LogManager.getLogger();

    public static void run() {
        start();
        waitingConnection();
    }

    private static void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            log.debug("Server started on port: {}", PORT);
        } catch (IOException e) {
            log.throwing(Level.ERROR, e);
            e.printStackTrace();
            try {
                serverSocket.close();
            } catch (IOException j) {
                j.printStackTrace();
            }
        }
    }

    private static void waitingConnection() {
        while (true) {
            log.debug("waiting connection......");
            try {
                socket = serverSocket.accept();
                log.debug("Client has connected - {}", socket.isConnected());
                log.debug("Client IP is - {}", socket.getInetAddress().getHostAddress());
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException j) {
                j.printStackTrace();
                try {
                    socket.close();
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
