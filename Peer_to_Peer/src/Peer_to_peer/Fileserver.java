/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Peer_to_peer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author YOB
 */
public class Fileserver {

    int port;
    ServerSocket serverSocket;
    ObjectInputStream yona;
    Socket socket;

    public Fileserver(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is Up");
            socket = serverSocket.accept();
        } catch (IOException ex) {
            Logger.getLogger(Fileserver.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        sendfile();
    }

    public void sendfile() {

        try {

            System.out.println("Connection madddd");
            yona = new ObjectInputStream(socket.getInputStream());
            HashMap sizemap = (HashMap) yona.readObject();
            String Name = sizemap.get("Name").toString();
            System.out.println("What is the value of x? " + Name);

            
            System.out.println("Accepted connection : " + socket);
            File transferFile = new File("C:\\Downloads\\" + Name);
            byte[] bytearray = new byte[(int) transferFile.length()];
            FileInputStream fin = new FileInputStream(transferFile);
            BufferedInputStream bin = new BufferedInputStream(fin);
            bin.read(bytearray, 0, bytearray.length);
            OutputStream os = socket.getOutputStream();
            System.out.println("Sending Files...");
            os.write(bytearray, 0, bytearray.length);
            os.flush();
            socket.close();
            System.out.println("File transfer complete");

        } catch (IOException ex) {
            Logger.getLogger(Fileserver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Fileserver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
