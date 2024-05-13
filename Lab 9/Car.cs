using System.Xml.Serialization;

[XmlType("car")]
public class Car
{
    public Car() { }
    public Car(string Model, Engine engine, int year)
    {
        this.Model = Model;
        this.Motor = engine;
        this.Year = year;
    }
    [XmlElement("model")]
    public string Model { get; set; }
    [XmlElement("engine")]
    public Engine Motor { get; set; }
    [XmlElement("year")]
    public int Year { get; set; }
}