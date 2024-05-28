using System.Collections.Concurrent;
using System.Net;
using System.Net.Sockets;
using System.Text;

namespace Server
{
    public class Program
    {
        private UdpClient _udpConnectionServer;
        private UdpClient _udpServer;
        private List<IPEndPoint> _udpClients;
        private BlockingCollection<KeyValuePair<byte, byte[]>> _messagesCollection;

        private static void Main()
        {
            var program = new Program
            {
                _udpConnectionServer = new UdpClient(9),
                _udpServer = new UdpClient(99),
                _udpClients = new List<IPEndPoint>(),
                _messagesCollection = new BlockingCollection<KeyValuePair<byte, byte[]>>(
                    new ConcurrentBag<KeyValuePair<byte, byte[]>>())
            };

            var connectionTask = Task.Factory.StartNew(() => program.WaitForClients());
            var collectDataTask = Task.Factory.StartNew(() => program.ResolveData());
            var sendDataTask = Task.Factory.StartNew(() => program.SendData());

            Console.WriteLine($"Server started at port {((IPEndPoint)program._udpConnectionServer.Client.LocalEndPoint).Port}");

            Task.WaitAll(connectionTask, collectDataTask, sendDataTask);
        }

        private void SendData()
        {
            try
            {
                while (true)
                {
                    var data = _messagesCollection.Take();
                    byte[] message = new byte[1 + data.Value.Length];
                    message[0] = data.Key;
                    Buffer.BlockCopy(data.Value, 0, message, 1, data.Value.Length);
                    _udpClients.ForEach(client => _udpServer.Send(message, message.Length, client));

                }

            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                Console.WriteLine(e.StackTrace);
            }
        }

        private void ResolveData()
        {
            try
            {
                while (true)
                {
                    IPEndPoint endPoint = new IPEndPoint(IPAddress.Any, 0);
                    byte[] bytes = _udpServer.Receive(ref endPoint);
                    var client = _udpClients.FindIndex(x => x.Equals(endPoint));
                    if (client == -1)
                    {
                        Console.WriteLine($"Unkonwn client {endPoint} wanted to join and the connection was terminated.");
                    }
                    else
                    {
                        switch (bytes[0])
                        {
                            case 0x01:
                                {
                                    // capitalise the strin
                                    string message = Encoding.ASCII.GetString(bytes, 1, bytes.Length - 1);
                                    Console.WriteLine($"Client {endPoint} sent: {message}");
                                    byte[] new_bytes = Encoding.ASCII.GetBytes(message.ToUpper());
                                    for (int i = 0; i < new_bytes.Length; i++)
                                    {
                                        bytes[i + 1] = new_bytes[i];
                                    }
                                    break;
                                }   
                            default:
                                Console.WriteLine("OTHER");
                                continue;
                        }
                        _messagesCollection.Add(new KeyValuePair<byte, byte[]>((byte)client, bytes));
                    }

                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                Console.WriteLine(e.StackTrace);
            }

        }

        private void WaitForClients()
        {
            try
            {
                while (true)
                {
                    IPEndPoint endPoint = new IPEndPoint(IPAddress.Any, 0);
                    byte[] bytes = _udpConnectionServer.Receive(ref endPoint);
                    string message = Encoding.ASCII.GetString(bytes);
                    if (message.Equals("connect"))
                    {
                        Console.WriteLine($"{endPoint} connected");
                        _udpClients.Add(endPoint);
                        byte[] messageBack = BitConverter.GetBytes((short)((IPEndPoint)_udpServer.Client.LocalEndPoint).Port);
                        _udpConnectionServer.Send(messageBack, messageBack.Length, endPoint);
                    }
                    else if (message.Equals("disconnect"))
                    {
                        _udpClients.Remove(endPoint);
                        Console.WriteLine($"{endPoint} disconnected");
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                Console.WriteLine(e.StackTrace);
            }
        }
    }

}
