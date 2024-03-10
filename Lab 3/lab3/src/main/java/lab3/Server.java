package lab3;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static int port = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port);) {

            System.out.println("Server started on port 8080");
            while (true) {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket fromClientSocket = serverSocket.accept();
                
                // create a new thread to handle the connection
                ConnectionHandler handler = new ConnectionHandler(fromClientSocket);
                Thread t = new Thread(handler);
                t.start();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
