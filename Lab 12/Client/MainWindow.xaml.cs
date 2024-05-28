using System.Diagnostics;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Windows;
using System.Windows.Input;

namespace Client
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private bool _isConnected;
        private UdpClient _udpClient;
        private IPEndPoint _ipEndPoint;
        private Task _task;

        public MainWindow()
        {
            InitializeComponent();
            _udpClient = new UdpClient();
            _isConnected = false;
            ButtonConnect.IsEnabled = true;
            ButtonDisconnect.IsEnabled = false;
            TextBoxStatus.Text = "Disconnected";
            TextBoxIp.IsEnabled = true;
            TextBoxPort.IsEnabled = true;
        }

        private void ButtonClick_Connect(object sender, RoutedEventArgs e)
        {
            if (!_isConnected)
            {
                _udpClient.Connect(TextBoxIp.Text, port: Int32.Parse(TextBoxPort.Text));
                _udpClient.Send(Encoding.ASCII.GetBytes("connect"), 7);

                _ipEndPoint = new IPEndPoint(IPAddress.Any, 0);
                var data = _udpClient.Receive(ref _ipEndPoint);
                _ipEndPoint.Port = BitConverter.ToInt16(data, 0);
                _udpClient.Connect(_ipEndPoint);
                ChangeConnectionState(true);
                _task = Task.Factory.StartNew(WaitForPaintData);
            }
        }

        private void ButtonClick_Disconnect(object sender, RoutedEventArgs e)
        {
            if (_isConnected)
            {
                _udpClient.Connect(TextBoxIp.Text, Int32.Parse(TextBoxPort.Text));
                _udpClient.Send(Encoding.ASCII.GetBytes("disconnect"), 10);
                _udpClient.Close();

                _task.Wait();
                _udpClient = new UdpClient();
                ChangeConnectionState(false);
            }
        }

        private void ChangeConnectionState(bool status)
        {
            _isConnected = status;
            ButtonConnect.IsEnabled = !status;
            ButtonDisconnect.IsEnabled = status;
            TextBoxStatus.Text = status ? "Connected" : "Disconnected";
            TextBoxIp.IsEnabled = !status;
            TextBoxPort.IsEnabled = !status;
        }

        private void WaitForPaintData()
        {
            try
            {
                while (_isConnected)
                {
                    IPEndPoint endPoint = new IPEndPoint(IPAddress.Any, 0);
                    Debug.WriteLine("Waiting");
                    byte[] bytes = _udpClient.Receive(ref endPoint);
                    Debug.WriteLine("Got something!");
                    byte id = bytes[0];
                    MenageIncomingMessage(bytes, id);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                Console.WriteLine(e.StackTrace);
            }
        }

        private void MenageIncomingMessage(byte[] bytes, byte id)
        {
            switch (bytes[1])
            {
                case 0x01:
                    {
                        this.Dispatcher.Invoke(() =>
                        {
                            string message = Encoding.ASCII.GetString(bytes, 2, bytes.Length - 2);
                            TextBoxMessage.Text = message + Environment.NewLine;
                        });
                        break;
                    }
            }
        }

        private void TextChanged(object sender, KeyEventArgs e)
        {
            if (_isConnected && e.Key == Key.Enter)
            {
                byte[] bytes = new byte[TextBoxMessage.Text.Length + 1];
                bytes[0] = 0x01;
                Encoding.ASCII.GetBytes(TextBoxMessage.Text).CopyTo(bytes, 1);
                _udpClient.Send(bytes, bytes.Length);
            }
        }
    }
}