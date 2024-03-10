package lab3;

import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private Socket ClientSocket;

    public ConnectionHandler(Socket ClientSocket) {
        this.ClientSocket = ClientSocket;
    }

    public void run() {
        try (
                java.io.InputStream in = ClientSocket.getInputStream();
                java.io.ObjectInputStream objectIn = new java.io.ObjectInputStream(in);
                java.io.OutputStream out = ClientSocket.getOutputStream();
                java.io.ObjectOutputStream objectOut = new java.io.ObjectOutputStream(out);) {

            System.out.println("Connection established");
            System.out.println("Sending to a client a message 'ready'");
            objectOut.writeObject("ready");

            // get a number of objects to receive from the client
            int n = (int) objectIn.readObject();
            System.out.println("Receiving " + n + " objects from the client");

            // send to a client message 'ready for messages'
            objectOut.writeObject("ready for messages");

            // receive n objects from the client
            for (int i = 0; i < n; i++) {
                Object obj = objectIn.readObject();
                System.out.println("Received object: " + obj);
            }
            System.out.println("All objects received");

            // send to a client a message 'finished'
            objectOut.writeObject("finished");

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
