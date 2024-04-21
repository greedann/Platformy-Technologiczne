using System.Windows;
using MessageBox = System.Windows.MessageBox;
using System.Text.RegularExpressions;

namespace Lab_8
{
    /// <summary>
    /// Interaction logic for FolderWindow.xaml
    /// </summary>
    public partial class FolderWindow : Window
    {
        public event EventHandler<CustomEventArgs> RaiseCustomEvent;
        public FolderWindow()
        {
            InitializeComponent();
        }

        private void OkClick(object sender, RoutedEventArgs e)
        {
            if ((bool)File1.IsChecked)
            {
                if (Regex.IsMatch(TextBox1.Text, "[a-zA-z_~-]{1,8}.[php|html|txt]"))
                {
                    String output = "file" + "|" + TextBox1.Text + "|" +
                   ((bool)r1.IsChecked ? "r" : "-") + ";" +
                   ((bool)a1.IsChecked ? "a" : "-") + ";" +
                   ((bool)s1.IsChecked ? "s" : "-") + ";" +
                   ((bool)h1.IsChecked ? "h" : "-") + ";";
                    RaiseCustomEvent(this, new CustomEventArgs(output));
                    Close();
                }
                else
                {
                    MessageBox.Show("Incorrect filename! It must have next form: <1-8 digits, letters, underlines, minuses or tildas>.<php or txt or html>");
                }


            }
            else
            {
                String output = "folder" + "|" + TextBox1.Text + "|" +
                  ((bool)r1.IsChecked ? "r" : "-") + ";" +
                  ((bool)a1.IsChecked ? "a" : "-") + ";" +
                  ((bool)s1.IsChecked ? "s" : "-") + ";" +
                  ((bool)h1.IsChecked ? "h" : "-") + ";";
                RaiseCustomEvent(this, new CustomEventArgs(output));
                Close();
            }

        }

        private void CancelClick(object sender, RoutedEventArgs e)
        {
            RaiseCustomEvent(this, new CustomEventArgs("none"));
            Close();
        }
    }

    public class CustomEventArgs : EventArgs
    {
        public CustomEventArgs(string s)
        {
            msg = s;
        }
        private string msg;
        public string Message
        {
            get { return msg; }
        }
    }
}
