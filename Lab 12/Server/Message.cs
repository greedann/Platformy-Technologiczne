using System;

public class Message
{
    public string Text { get; set; }
    public string Author { get; set; }
    public DateTime Time { get; set; }

    public override string ToString()
    {
        return $"[{Time}] {Author}: {Text}";
    }
}
