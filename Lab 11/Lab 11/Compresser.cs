using System.IO;
using System.IO.Compression;
using System.Windows;

namespace Lab_11
{
    public class Compresser
    {
        private DirectoryInfo dir;

        public Compresser(string path)
        {
            dir = new DirectoryInfo(path);
        }

        public void Compress()
        {
            List<Task> filesToCompress = new List<Task>();
            foreach (var file in this.dir.GetFiles())
            {
                filesToCompress.Add(Task.Factory.StartNew(() => CompressFile(file)));
            }

            Task.WaitAll(filesToCompress.ToArray());
            MessageBox.Show("Compression completed.");
        }

        public void Decompress()
        {
            List<Task> filesToDecompress = new List<Task>();
            foreach (var file in dir.GetFiles("*.gz"))
            {
                filesToDecompress.Add(Task.Factory.StartNew(() => DecompressFile(file)));
            }

            Task.WaitAll(filesToDecompress.ToArray());
            MessageBox.Show("Decompression completed.");
        }

        private static void CompressFile(FileInfo file)
        {
            using (FileStream originalFileStream = file.OpenRead())
            {
                if ((File.GetAttributes(file.FullName) &
                     FileAttributes.Hidden) != FileAttributes.Hidden & file.Extension != ".gz")
                {
                    using (FileStream compressedFileStream = File.Create(file.FullName + ".gz"))
                    {
                        using (GZipStream compressionStream = new GZipStream(compressedFileStream,
                                   CompressionMode.Compress))
                        {
                            originalFileStream.CopyTo(compressionStream);

                        }
                    }
                    FileInfo info = new FileInfo($"{file.Directory.FullName}{Path.DirectorySeparatorChar}{file.Name}.gz");
                }

            }

        }
        private static void DecompressFile(FileInfo file)
        {
            using (FileStream originalFileStream = file.OpenRead())
            {
                string currentFileName = file.FullName;
                string newFileName = currentFileName.Remove(currentFileName.Length - file.Extension.Length);

                using (FileStream decompressedFileStream = File.Create(newFileName))
                {
                    using (GZipStream decompressionStream = new GZipStream(originalFileStream, CompressionMode.Decompress))
                    {
                        decompressionStream.CopyTo(decompressedFileStream);
                    }
                }
            }

        }



    }
}
