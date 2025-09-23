package javasocket;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;

public class clientconnection {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private static final int PORT = 1000;
    private static final String STOP_STRING = "##";

    public myfileserver(){
        try{
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataInputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile() {
        try{
        sendMenu();
        int index = getSelectedFileIndex();
        sendSelectedFile(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendSelectedFile(int index) throws IOException {
        File[] fileList = new File(myfileserver.FILES_PATH).listFiles();
        File selectedFile =fileList[index];
        List<String> fileLines = Files.readAllLines(selectedFile.toPath());
        String fileContnet = String.join("\n",fileLines);
        out.writeUTF(fileContnet);
    }

    private int getSelectedFileIndex() {
        String input = in.readUTF();
        return Integer.parseInt(input)-1;
    }

    private void sendMenu() throws IOException {
        String menu = "** Files **\n";
        File[] fileList = new File(myfileserver.FILES_PATH).listFiles();
        out.writeUTF("fileList.length");
        for (int i = 0; i < fileList.length; i++) {
            menu += String.format("* %d -  %s\n", i + 1, fileList[i].getName());
        }
        out.writeUTF(menu);
        }
}

