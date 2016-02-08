/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Peer_to_peer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author YOB
 */
public class Server1 {

    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/peer1";
    private static Connection connection = null;
    public static Statement stmt = null;
    private static ResultSet resultSet = null;
    private Socket sock;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ObjectOutputStream list;

    public void start() {
        try {
            sock = new Socket("127.0.0.1", 3000);
            System.out.println("Connection Established");
            connection = DriverManager.getConnection(DATABASE_URL, "root", "");
            System.out.println("Database Established");

        } catch (SQLException ex) {
            Logger.getLogger(Server1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server1.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            result();
        }
    }

    public void result() {
        try {
            ois = new ObjectInputStream(sock.getInputStream());
            HashMap t1 = (HashMap) ois.readObject();
            String controller1 = t1.get("Controller").toString();
            System.out.println("This is the controller: " + controller1);

            if (controller1.equals("Register")) {
                String Name = t1.get("Name").toString();
                String Hash = t1.get("Hash").toString();
                int size = Integer.parseInt(t1.get("size").toString());
                System.out.println("The file size is :" + size);
                String Ip = t1.get("Ip").toString();
                int Port = Integer.parseInt(t1.get("Port").toString());
                System.out.println("Name: "+Name);
                System.out.println("Name: "+Hash);
                System.out.println("Name: "+size);
                System.out.println("Name: "+Ip);
                stmt = connection.createStatement();
                String sql = "insert into file values('" + Hash + "','" + Name + "'," + size + ",'" + Ip + "'," + Port + ")";
                int result = stmt.executeUpdate(sql);
                if (result > 0) {
                    System.out.println("Chunks Registeration is Successful");
                }
               
                else {
                    System.err.println("Error Registering");
                }

            } else if (controller1.equals("list")) {

                stmt = connection.createStatement();
                String sqll = "select * from file";
                resultSet = stmt.executeQuery(sqll);
                int x = 0;
                while (resultSet.next()) {
                    x++;

                }
                HashMap newmap = new HashMap();
                newmap.put("size", x);
                oos = new ObjectOutputStream(sock.getOutputStream());
                oos.writeObject(newmap);
                oos.flush();
                stmt = connection.createStatement();
                String sqlsend = "select * from file";
                resultSet = stmt.executeQuery(sqlsend);
                System.out.println("Send");
                while (resultSet.next()) {
                    System.out.println("trying to send");
                    oos = new ObjectOutputStream(sock.getOutputStream());
                    HashMap newhash = new HashMap();
                    String Hash = resultSet.getString(1);
                    String Name = resultSet.getString(2);
                    String Ip = resultSet.getString(4);
                    newhash.put("Name", Name);
                    newhash.put("Hash", Hash);
                    newhash.put("Ip Address", Ip);
                    System.out.println(resultSet.getString(1));
                    System.out.println(resultSet.getString(2));
                    System.out.println(resultSet.getString(3));
                    oos.writeObject(newhash);
                    oos.flush();
                    newhash.clear();
                }

            } else if (controller1.equals("Delete")) {
                String ip = t1.get("Ip").toString();
                stmt = connection.createStatement();

                String sql = "DELETE FROM file WHERE ip_address=" + "'" + ip + "'";
                int result = stmt.executeUpdate(sql);
                if (result > 0) {
                    System.out.println("Successfully deleted");
                } else {
                    System.err.println("Error in deleting");
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Server1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Server1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws Exception {
        Server1 server = new Server1();
        server.start();
    }
}
