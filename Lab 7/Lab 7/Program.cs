using System.Text;
using System.Runtime.Serialization.Formatters.Binary;

namespace ExtensionMethods
{
    public static class DirectoryInfoExtensions
    {
        public static DateTime GetOldestFile(this DirectoryInfo di, DateTime oldest)
        {
            DateTime current = Directory.GetCreationTime(di.FullName);
            if (current < oldest)
            {
                oldest = current;
            }
            var dirs = Directory.GetDirectories(di.FullName);
            foreach (var dir in dirs)
            {
                DirectoryInfo dis = new DirectoryInfo(dir);
                DateTime dt = dis.GetOldestFile(oldest);
                if (dt < oldest)
                {
                    oldest = dt;
                }
            }
            string[] files = Directory.GetFiles(di.FullName);
            foreach (var file in files)
            {
                DateTime dt = File.GetCreationTime(file);
                if (dt < oldest)
                {
                    oldest = dt;
                }
            }
            return oldest;
        }
    }

    public static class FileSystemInfoExtensions
    {
        public static string GetAttr(this FileSystemInfo file)
        {
            StringBuilder ret = new StringBuilder("----");
            if (file.Attributes.HasFlag(FileAttributes.ReadOnly))
            {
                ret[0] = 'r';
            }
            if (file.Attributes.HasFlag(FileAttributes.Archive))
            {
                ret[1] = 'a';
            }
            if (file.Attributes.HasFlag(FileAttributes.Hidden))
            {
                ret[2] = 'h';
            }
            if (file.Attributes.HasFlag(FileAttributes.System))
            {
                ret[3] = 's';
            }
            return ret.ToString();
        }
    }

}






namespace Lab7
{
    using ExtensionMethods;
    class Lab7
    {
        [Serializable]
        public class StrComp : IComparer<string>
        {
            public int Compare(string? first, string? second)
            {
                if (second != null && first != null && first.Length > second.Length)
                {
                    return 1;
                }
                else if (second != null && first != null && first.Length == second.Length)
                {
                    return string.Compare(first, second);
                }
                return -1;
            }
        }

        static void Serialize(SortedList<string, long> sl, string filename)
        {
            Stream ms = File.OpenWrite(filename);
            BinaryFormatter formatter = new BinaryFormatter();
            formatter.Serialize(ms, sl);
            ms.Flush();
            ms.Close();
            ms.Dispose();
        }

        static SortedList<string, long> Deserialize(string filename)
        {
            BinaryFormatter formatter = new BinaryFormatter();
            FileStream fs = File.Open(filename, FileMode.Open);
            object obj = formatter.Deserialize(fs);
            SortedList<string, long> sl = (SortedList<string, long>)obj;
            fs.Flush();
            fs.Close();
            fs.Dispose();
            return sl;
        }

        public static void Main(string[] args)
        {
            if (args.Length == 0)
            {
                Console.WriteLine("Usage: Lab7.exe <path>");
                return;
            }
            DisplayDirectory(args[0]);

            DirectoryInfo di = new DirectoryInfo(args[0]);
            DateTime dt = di.GetOldestFile(DateTime.Now);
            Console.WriteLine("\nOldest file: {0}\n", dt);

            SortedList<string, long> sl = new SortedList<string, long>(new StrComp());
            var dirs = Directory.GetDirectories(args[0]);
            foreach (var dir in dirs)
            {
                sl.Add(dir, Convert.ToInt64(Directory.GetDirectories(dir).Length + Directory.GetFiles(dir).Length));
            }
            var files = Directory.GetFiles(args[0]);
            foreach (var file in files)
            {
                FileInfo fi = new FileInfo(file);
                sl.Add(file, fi.Length);
            }
            string pth = @"D:\Test.bin";
            Serialize(sl, pth);
            SortedList<string, long> readSl = Deserialize(pth);
            foreach (KeyValuePair<string, long> kvp in readSl)
            {
                Console.WriteLine("{0} - {1}", kvp.Key, kvp.Value);
            }
        }

        static void Padding(int level)
        {
            for (int i = 0; i < level; i++)
            {
                Console.Write("  ");
            }
        }

        static void DisplayDirectory(string path, int level = 0)
        {
            Padding(level);
            DirectoryInfo di = new DirectoryInfo(path);
            Console.WriteLine("{0} {1} amound:{2}", path, di.GetAttr(), Directory.GetDirectories(path).Length + Directory.GetFiles(path).Length);
            var dirs = Directory.GetDirectories(path);
            foreach (var dir in dirs)
            {
                DisplayDirectory(dir, level + 1);
            }
            var files = Directory.GetFiles(path);
            foreach (var file in files)
            {
                FileInfo fi = new FileInfo(file);
                Padding(level + 1);
                Console.WriteLine("{0} {1} size:{2}", file, fi.GetAttr(), fi.Length);
            }
        }
    }
}