using System;
using System.Net.Sockets;
using System.Text;
using System.Text.Json;
using System.Threading;

public class Client
{
    private static TcpClient client;
    private static NetworkStream stream;

    public static void Main(string[] args)
    {
        client = new TcpClient("127.0.0.1", 5000);
        stream = client.GetStream();

        Thread receiveThread = new Thread(new ThreadStart(ReceiveMessages));
        receiveThread.Start();

        Console.WriteLine("Enter your name:");
        string author = Console.ReadLine();

        while (true)
        {
            string text = Console.ReadLine();
            Message message = new Message
            {
                Text = text,
                Author = author,
                Time = DateTime.Now
            };
            SendMessage(message);
        }
    }

    private static void SendMessage(Message message)
    {
        string messageData = JsonSerializer.Serialize(message);
        byte[] buffer = Encoding.UTF8.GetBytes(messageData);
        stream.Write(buffer, 0, buffer.Length);
    }

    private static void ReceiveMessages()
    {
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = stream.Read(buffer, 0, buffer.Length)) != 0)
        {
            string receivedData = Encoding.UTF8.GetString(buffer, 0, bytesRead);
            Message message = JsonSerializer.Deserialize<Message>(receivedData);
            Console.WriteLine(message.ToString());
        }
    }
}
