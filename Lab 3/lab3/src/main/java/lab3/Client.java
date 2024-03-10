package lab3;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class Client {
    private static int port = 8080;
    private static String message;

    public static void main(String[] args) {
        try (
                java.net.Socket socket = new java.net.Socket("localhost", port);
                OutputStream out = socket.getOutputStream();
                ObjectOutputStream objectOut = new ObjectOutputStream(out);
                InputStream in = socket.getInputStream();
                ObjectInputStream objectIn = new ObjectInputStream(in);
                Scanner scanner = new Scanner(System.in);) {

            System.out.println("Connected to server on port " + port);
            System.out.println("Waiting for server to send a message 'ready'");
            while (true) {
                message = objectIn.readObject().toString();
                if (message.equals("ready")) {
                    System.out.println("Server sent: " + message);
                    break;
                }
            }
            // connection established

            // send a number of objects to the server
            System.out.println("Enter a number of objects to send to the server: ");
            message = scanner.nextLine();
            int n = Integer.parseInt(message);
            objectOut.writeObject(n);
            System.out.println("Sending to the server a number of objects: " + n);

            // wait for a message 'ready for messages'
            while (true) {
                message = objectIn.readObject().toString();
                if (message.equals("ready for messages")) {
                    System.out.println("Server sent: " + message);
                    break;
                }
            }

            // send n objects to the server
            for (int i = 0; i < n; i++) {
                System.out.println("Enter an object to send to the server: ");
                message = scanner.nextLine();
                Messsage message_to_send = new Messsage(i, message);
                objectOut.writeObject(message_to_send);
                System.out.println("Sending to the server an object: " + message_to_send);
            }

            // wait for a message 'finished'
            while (true) {
                message = objectIn.readObject().toString();
                if (message.equals("finished")) {
                    System.out.println("Server sent: " + message);
                    break;
                }
            }

            System.out.println("All objects sent");

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }
}
