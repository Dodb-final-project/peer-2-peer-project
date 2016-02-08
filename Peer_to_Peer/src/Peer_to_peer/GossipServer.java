/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Peer_to_peer;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GossipServer {

    //static final String DATABASE_URL = "jdbc:mysql://localhost:3306/peer";
    private static Connection connection = null;
    public static Statement stmt = null;
    private static ResultSet resultSet = null;
    public static ServerSocket sersock = null;
    public static Socket sock = null;
    public static Socket sockcli = null;
    public static Socket sockcli2=null;
    public static Socket replication=null;
    public static OutputStream ostream = null;
    public static ObjectInputStream ois = null;
    public static ObjectOutputStream forserver1 = null;
    private InputStream iso;
    private InputStream istream;

    public void start() throws SQLException {

        //connection = DriverManager.getConnection(DATABASE_URL, "root", "");
        try {
            sersock = new ServerSocket(3000);
            sockcli = sersock.accept();
            System.out.println("Connection Established With Server 1");
            sockcli2=sersock.accept();
            System.out.println("Connection Established With Server 2");
            replication=sersock.accept();
            System.out.println("Connection Established With Server 3");
        } catch (IOException ex) {
            Logger.getLogger(GossipServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Server  ready for Communication");
        while (true) {
            try {
                System.out.println("Socket accepted");
                sock = sersock.accept();
                System.out.println("Socket accepted");
                handleclient handle = new handleclient(sock);
                new Thread(handle).start();
            } catch (IOException ex) {
                Logger.getLogger(GossipServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void list() {

    }

    public static void main(String[] args) throws Exception {
        GossipServer server = new GossipServer();
        server.start();
    }

    public class servers implements Runnable {

        @Override
        public void run() {

        }
    }

    public class list implements Runnable {

        Socket socketlist;

        public list(Socket socketlist) {
            this.socketlist = socketlist;

        }

        @Override
        public void run() {

            try {
                ObjectOutputStream yoforserver;
                ObjectInputStream yoforserverin;
                ObjectOutputStream outyoforserver1;
                ObjectInputStream  inyoforserver1;
                HashMap Cli = new HashMap();
                Cli.put("Controller", "list");
                yoforserver = new ObjectOutputStream(sockcli.getOutputStream());
                yoforserver.writeObject(Cli);
                yoforserver.flush();
                //
                HashMap Cli2 = new HashMap();
                Cli2.put("Controller", "list");
                outyoforserver1 = new ObjectOutputStream(sockcli2.getOutputStream());
                outyoforserver1.writeObject(Cli2);
                outyoforserver1.flush();
                //

                yoforserverin = new ObjectInputStream(sockcli.getInputStream());
                HashMap sizemap = (HashMap) yoforserverin.readObject();
                int size = Integer.parseInt(sizemap.get("size").toString());
                System.out.println("What is the value of x? " + size);
                //
                inyoforserver1 = new ObjectInputStream(sockcli2.getInputStream());
                HashMap sizemapforserver2 = (HashMap) inyoforserver1.readObject();
                int size1 = Integer.parseInt(sizemapforserver2.get("size").toString());
                System.out.println("What is the value of x for server 2? " + size1);
                //
                
                ObjectOutputStream me = new ObjectOutputStream(socketlist.getOutputStream());
                HashMap sizetotheclient = new HashMap();
                System.out.println("Hfff i have to check again the size is: " + size);
                sizetotheclient.put("size", size);
                me.writeObject(sizetotheclient);
                me.flush();
                
                
                
                
                
                
                int y = 0;
                String Name = "";
                int Port = 0;
                int sizeoffile=0;
                int n=0;
                String Name1="";
                String Ip="";
                String Hash="";
                while (y < size) {
                    System.out.println("tring to accept");
                    yoforserverin = new ObjectInputStream(sockcli.getInputStream());
                    HashMap input = (HashMap) yoforserverin.readObject();
                    Name = input.get("Name").toString();
                    Hash=input.get("Hash").toString();
                    Ip=input.get("Ip Address").toString();
                    
                    
                    System.out.println("          Student List        ");
                    System.out.println(input.size());
                    System.out.println("Name of me: " + Name);
                    System.out.println("Ip Address: " + Ip);
                    System.out.println("Hash : " + Hash);
                    System.out.println("*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&");
                   
                    
                    
                    inyoforserver1=new ObjectInputStream(sockcli2.getInputStream());
                    HashMap inputhashserver2=(HashMap) inyoforserver1.readObject();
                    
                    Name1=inputhashserver2.get("Name").toString();
                    Hash=inputhashserver2.get("Hash").toString();
                    sizeoffile=Integer.parseInt(inputhashserver2.get("size").toString());
                    Port=Integer.parseInt(inputhashserver2.get("Port").toString());
                    
                    System.out.println("The Name of the student is : "+Name1);
                    System.out.println("The Port of the peer is : "+Port);
                    System.out.println("The Hash of the File is : "+Hash);
                    me = new ObjectOutputStream(socketlist.getOutputStream());
                    HashMap newhash = new HashMap();
                    newhash.put("Name", Name);
                    newhash.put("Hash", Hash);
                    newhash.put("size", sizeoffile);
                    newhash.put("Ip", Ip);
                    newhash.put("Port", Port);
                    
                    me.writeObject(newhash);
                    me.flush();
                    newhash.clear();
                    input.clear();
                    inputhashserver2.clear();
                    
                    
                    y++;
                }
                
                
                
                
              

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    public void Register(String file_name, int file_size, String hash,String ip, int port) throws IOException {

                ObjectOutputStream  forserver1;
                ObjectOutputStream  forserver2;
                ObjectOutputStream  forreplication;
                HashMap Cli = new HashMap();
                String filename = file_name.substring(file_name.lastIndexOf("\\") + 1);
                Cli.put("Controller","Register");
                Cli.put("Name", filename);
                Cli.put("Hash",hash);
                Cli.put("size", file_size);
                System.out.println("ASDHAKSDHAKJSHDKAJSHDKAJSHDKAJSDHKASJD:"+file_size);
                Cli.put("Ip",ip);
                Cli.put("Port",port);
                
                forserver1 = new ObjectOutputStream(sockcli.getOutputStream());
                forserver1.writeObject(Cli);
                forserver1.flush();
                
                forserver2 = new ObjectOutputStream(sockcli2.getOutputStream());
                forserver2.writeObject(Cli);
                forserver2.flush();
                
                
                forreplication = new ObjectOutputStream(replication.getOutputStream());
                forreplication.writeObject(Cli);
                forreplication.flush();
                
                

       

    }
    
    public void Delete(String ip){
        try {
            ObjectOutputStream  forserver1;
            ObjectOutputStream  forserver2;
            ObjectOutputStream  forreplication;
            HashMap Cli = new HashMap();
            
            Cli.put("Controller","Delete");
            Cli.put("Ip", ip);
            
            
            forserver1 = new ObjectOutputStream(sockcli.getOutputStream());
            forserver1.writeObject(Cli);
            forserver1.flush();
            
            forserver2 = new ObjectOutputStream(sockcli2.getOutputStream());
            forserver2.writeObject(Cli);
            forserver2.flush();
            
            
            forreplication = new ObjectOutputStream(replication.getOutputStream());
            forreplication.writeObject(Cli);
            forreplication.flush();
        } catch (IOException ex) {
            Logger.getLogger(GossipServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    public class handleclient implements Runnable {

        Socket socket;
        ObjectInputStream ois;

        public handleclient(Socket socket) {

            this.socket = socket;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    ois = new ObjectInputStream(socket.getInputStream());
                    HashMap t = (HashMap) ois.readObject();
                    String controller = t.get("controller").toString();
                    System.out.println("This is the controller: "+controller);
                    if (controller.equals("Search")) {
                        list yona = new list(socket);
                        new Thread(yona).start();

                    }
                    else if(controller.equals("Please")){
                    String name=t.get("file_name").toString();
                    int file_size=Integer.parseInt(t.get("file_size").toString());
                    System.out.println("This is kira's file size: "+file_size);
                    int port=Integer.parseInt(t.get("port").toString());
                    String ip=t.get("ip_address").toString();
                    String hash=t.get("hash").toString();
                     System.out.println(name);
                     System.out.println(file_size);
                     System.out.println(port);
                    
                     Register(name,file_size,hash,ip,port);
                     System.out.println(name);
                     System.out.println(file_size);
                     System.out.println(port);
                    
                    }
                    else if(controller.equals("Delete")){
                    String ip=t.get("Ip").toString();
                    Delete(ip);
                        
                    }

                    System.out.println(controller);

                } catch (IOException ex) {
                    Logger.getLogger(GossipServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GossipServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }
}
