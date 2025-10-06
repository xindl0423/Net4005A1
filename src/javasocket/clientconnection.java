package javasocket;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;

public class clientconnection {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private static final int PORT = 2020;
    private static final String STOP_STRING = "##";

    public clientconnection(Socket socket){
        this.socket = socket;
        try{
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out =new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile() {
        String fileName = "";
        boolean success = false;
        String clientIP = socket.getInetAddress().getHostAddress();

        try {
            // Increment total requests before handling
            myfileserver.incrementTotalRequests();

            // Send the file menu
            sendMenu();

            // Ask client for file name
            out.writeUTF("Enter the filename you want to download:");
            fileName = in.readUTF();

            File file = new File(myfileserver.FILES_PATH + "/" + fileName);

            // Check if file exists
            if (file.exists() && file.isFile()) {
                myfileserver.incrementSuccessfulRequests();
                success = true;
                out.writeUTF("File found. Sending content...\n");

                sendFileContent(file);
            } else {
                out.writeUTF("File not found on server.\n");
            }

            // Send <N, M> statistics
            int total = myfileserver.getTotalRequests();
            int successful = myfileserver.getSuccessfulRequests();
            out.writeUTF("Server stats <Total Requests: " + total + ", Successful: " + successful + ">");

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Log the request regardless of success or failure
            myfileserver.logRequest(clientIP, fileName, success);
        }
    }

    private void sendFileContent(File file) throws IOException {
        List<String> fileLines = Files.readAllLines(file.toPath());
        String fileContent = String.join("\n", fileLines);
        out.writeUTF(fileContent);
    }

    private void sendMenu() throws IOException {
        String menu = "** Files on Server **\n";
        File[] fileList = new File(myfileserver.FILES_PATH).listFiles();

        if (fileList == null || fileList.length == 0) {
            out.writeUTF("No files available on the server.\n");
            return;
        }

        for (int i = 0; i < fileList.length; i++) {
            menu += String.format("* %d - %s\n", i + 1, fileList[i].getName());
        }

        out.writeUTF(menu);
    }
}

