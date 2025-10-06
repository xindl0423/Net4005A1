package javasocket;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class myfileclient {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java myfileclient <server_IP> <server_port> <filename>");
            return;
        }

        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String filename = args[2];

        Socket socket = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        FileOutputStream fileOutputStream = null;

        try {
            // Connect to server
            socket = new Socket(serverIP, serverPort);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // Receive menu
            String menu = in.readUTF();

            // Receive prompt
            String prompt = in.readUTF();

            // Send the requested filename to server
            out.writeUTF(filename);

            // Receive file status response
            String fileStatus = in.readUTF();

            // Parse the response to determine if file was found
            boolean fileFound = fileStatus.contains("File found");

            if (fileFound) {
                System.out.println("File " + filename + " found at server");
            } else {
                System.out.println("File " + filename + " not found at server");
            }

            if (fileFound) {
                // Receive file content
                String fileContent = in.readUTF();

                // Save the file
                fileOutputStream = new FileOutputStream(filename);
                fileOutputStream.write(fileContent.getBytes());
                fileOutputStream.flush();

                System.out.println("Downloading file " + filename);
                System.out.println("Download complete");
            }

            // Receive and parse statistics
            String stats = in.readUTF();
            // Extract numbers from stats string
            String[] parts = stats.split("[<>]");
            if (parts.length >= 2) {
                String numbers = parts[1].replaceAll("[^0-9,]", "");
                String[] statsNumbers = numbers.split(",");
                if (statsNumbers.length >= 2) {
                    int totalRequests = Integer.parseInt(statsNumbers[0].replaceAll("[^0-9]", ""));
                    int successfulRequests = Integer.parseInt(statsNumbers[1].replaceAll("[^0-9]", ""));
                    System.out.println("Server handled " + totalRequests +
                            " requests, " + successfulRequests + " requests were successful");
                }
            } else {
                System.out.println(stats); // Fallback: print raw stats
            }

        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverIP);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number: " + args[1]);
        } finally {
            // Clean up resources
            try {
                if (fileOutputStream != null) fileOutputStream.close();
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
