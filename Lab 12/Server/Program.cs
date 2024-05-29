using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Text.Json;
using System.Threading;

public class Server
{
    private static List<TcpClient> clients = new List<TcpClient>();

    public static void Main(string[] args)
    {
        TcpListener server = new TcpListener(IPAddress.Any, 5000);
        server.Start();
        Console.WriteLine("Server started...");

        while (true)
        {
            TcpClient client = server.AcceptTcpClient();
            clients.Add(client);
            Console.WriteLine("Client connected...");
            Thread clientThread = new Thread(() => HandleClient(client));
            clientThread.Start();
        }
    }

    private static void HandleClient(TcpClient client)
    {
        NetworkStream stream = client.GetStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = stream.Read(buffer, 0, buffer.Length)) != 0)
        {
            string receivedData = Encoding.UTF8.GetString(buffer, 0, bytesRead);
            Message message = JsonSerializer.Deserialize<Message>(receivedData);
            message.Text = message.Text.ToUpper();
            BroadcastMessage(message);
        }

        clients.Remove(client);
        client.Close();
    }

    private static void BroadcastMessage(Message message)
    {
        string messageData = JsonSerializer.Serialize(message);
        byte[] buffer = Encoding.UTF8.GetBytes(messageData);

        foreach (var client in clients)
        {
            NetworkStream stream = client.GetStream();
            stream.Write(buffer, 0, buffer.Length);
        }

        Console.WriteLine(message.ToString());
    }
}
