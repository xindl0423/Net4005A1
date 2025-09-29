package javasocket;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class myfileserver {
    private ServerSocket serverSocket;
    public static final String FILES_PATH = "./ServerFiles";
    public static final int PORT = 1000;
    static int totalrequests = 0;
    static int successfulrequests = 0;
    final int MAX_THREADS = 10;

    private ExecutorService threadPool;

    public myfileserver() throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
            threadPool = Executors.newFixedThreadPool(MAX_THREADS);
            acceptconnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptconnections() throws IOException {
        while (true){
            Socket clientsocket = serverSocket.accept();
            if(clientsocket.isConnected())
                threadPool.submit(() ->{
                    clientconnection client = new clientconnection(clientsocket);
                    client.sendFile();
                } );
        }
    }

    public static synchronized void incrementTotalRequests() {
        totalrequests++;
    }
    public static synchronized void incrementSuccessfulRequests() {
        successfulrequests++;
    }

    public static synchronized int getTotalRequests() {
        return totalrequests;
    }

    public static synchronized int getSuccessfulRequests() {
        return successfulrequests;
    }

    public static void main(String[] args) throws IOException {
        new myfileserver();
    }
}