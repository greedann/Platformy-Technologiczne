using System.Xml.Serialization;

public class Engine
{

    public Engine() { }
    public Engine(double Displacement, double HorsePower, string Model)
    {
        this.HorsePower = HorsePower;
        this.Model = Model;
        this.Displacement = Displacement;
    }
    [XmlElement("horsePower")]
    public double HorsePower { get; set; }
    [XmlAttribute("model")]
    public string Model { get; set; }
    [XmlElement("displacement")]
    public double Displacement { get; set; }

}