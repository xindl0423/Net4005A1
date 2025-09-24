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
    int  totalrequests = 0;
    int successfulrequests = 0;
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
            if(clientsocket.isconnected())
                threadPool.submit(() ->{
                    clientconnection client = new clientconnection(clientsocket);
                    client.sendFile();
                } ).start();
        }
    }
    public static void main(String[] args){
        System.out.println();
    }

}