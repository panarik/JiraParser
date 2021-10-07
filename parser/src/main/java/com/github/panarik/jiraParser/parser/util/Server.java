package com.github.panarik.jiraParser.parser.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket serverSocket;
    private static Socket socket;
    private static final int PORT = 8090;
    private static DataInputStream in;
    private static DataOutputStream out;

    public static void run() {
        start();
        waitingConnection();
    }

    private static void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            Log.debug("Server started on port " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
            Log.debug(e.getMessage());
            try {
                serverSocket.close();
            } catch (IOException j) {
                j.printStackTrace();
            }
        }
    }

    private static void waitingConnection() {
        while (true) {
            Log.debug("waiting connection......");
            try {
                socket = serverSocket.accept();
                Log.debug("Client has connected - " + socket.isConnected());
                Log.debug("Client IP is - " + socket.getInetAddress().getHostAddress());
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
