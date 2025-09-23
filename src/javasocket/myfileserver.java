package javasocket;

import java.net.*;
import java.io.*;
import java.uti.concurrent.*;

public class myfileserver extend Thread{
    private ServerSocket serverSocket;
    public static final String FILES_PATH = "./ServerFiles";
    public static final int PORT = 1000;
    int  totalrequests = 0;
    int successfulrequests = 0;
    int MAX_THREADS = 10;

    public myfileserver() throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
            acceptconnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptconnections() throws IOException {
        while (true){
            Socket clientsocket = serverSocket.accept();
            if(clientsocket.isconnected())
                new Thread.( () ->{
                    clientconnection client = new clientconnection(clientsocket);
                    client.sendFile();
                } ).start();
        }
    }
    public static void main(String[] args){
        System.out.println();
    }

}