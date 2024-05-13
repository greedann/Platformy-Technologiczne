// See https://aka.ms/new-console-template for more information

using System.Xml.Serialization;
using System.Xml.Linq;
using System.Xml.XPath;




public class Program
{
    [XmlArray("cars")]
    static public List<Car> cars = new(){
          new("E250", new Engine(1.8, 204, "CGI"), 2009),
            new("E350", new Engine(3.5, 292, "CGI"), 2009),
            new("A6", new Engine(2.5, 187, "FSI"), 2012),
            new("A6", new Engine(2.8, 220, "FSI"), 2012),
            new("A6", new Engine(3.0, 295, "TFSI"), 2012),
            new("A6", new Engine(2.0, 175, "TDI"), 2011),
            new("A6", new Engine(3.0, 309, "TDI"), 2011),
            new("S6", new Engine(4.0, 414, "TFSI"), 2012),
            new("S8", new Engine(4.0, 513, "TFSI"), 2012)
    };

    public static void Main(string[] args)
    {
        Console.WriteLine("LINQ 1:");
        var query1 = cars
            .Where(car => car.Model == "A6")
            .Select(car => new
            {
                EngineType = car.Motor.Model == "TDI" ? "diesel" : "petrol",
                Hppl = car.Motor.HorsePower / car.Motor.Displacement
            });
        foreach (var car in query1)
        {
            Console.WriteLine(car);
        }
        Console.WriteLine("LINQ 2:");
        var query2 = query1.
            GroupBy(car => car.EngineType)
            .Select(group => new
            {
                EngineType = group.Key,
                AvgHppl = group.Average(car => car.Hppl)
            });
        foreach (var car in query2)
        {
            Console.WriteLine(car);
        }

        Console.WriteLine("Deserialization:");
        XmlRootAttribute root = new("cars");
        XmlSerializer serializer = new(typeof(List<Car>), root);
        TextWriter writer = new StreamWriter("CarsCollection.xml");
        serializer.Serialize(writer, cars);
        writer.Close();
        FileStream fileStream = new("CarsCollection.xml", FileMode.Open);
        List<Car>? result = null;
        if (serializer.Deserialize(fileStream) is List<Car> deserializedResult)
        {
            result = deserializedResult;
        }
        fileStream.Close();
        if (result != null)
        {
            foreach (var item in result)
            {
                Console.WriteLine("({0}, ({1}, {2}, {3}), {4})", item.Model, item.Motor.Displacement, item.Motor.Model,
                    item.Motor.HorsePower, item.Year);
            }
        }
        Console.WriteLine("avgHP:");
        XElement rootNode = XElement.Load("CarsCollection.xml");
        double avgHP = (double)rootNode.XPathEvaluate("sum(//engine[@model!='TDI']/horsePower) div count(//engine[@model!='TDI']/horsePower)");
        Console.WriteLine(avgHP);

        Console.WriteLine("unique models:");
        IEnumerable<XElement> models = rootNode.XPathSelectElements("//car/model[not(. = ../preceding-sibling::car/model)]");
        foreach (XElement model in models)
        {
            Console.WriteLine(model.Value);
        }


        CreateXmlFromLinq(cars);

        CreateTableFromLinq(cars);
        XDocument doc = XDocument.Load("CarsCollection.xml");
        foreach (XElement hp in doc.Descendants("horsePower"))
        {
            hp.Name = "hp";
        }
        foreach (XElement year in doc.Descendants("year"))
        {
            if (year.Parent != null)
            {
                foreach (XElement model in year.Parent.Descendants("model"))
                {
                    model.SetAttributeValue("year", year.Value);
                }
            }
        }
        doc.Descendants("year").Remove();
        doc.Save("CarsCollectionModified.xml");
    }

    private static void CreateXmlFromLinq(List<Car> myCars)
    {
        IEnumerable<XElement> nodes = from car in myCars
                                      select new XElement("car",
                                                  new XElement("model", car.Model),
                                                  new XElement("engine",
                                                      new XAttribute("model", car.Motor.Model),
                                                      new XElement("horsePower", car.Motor.HorsePower),
                                                      new XElement("displacement", car.Motor.Displacement)),
                                                  new XElement("year", car.Year)); // zapytanie LINQ
        XElement rootNode = new("cars", nodes); // stwórz węzeł
        rootNode.Save("CarsFromLinq.xml");
    }
    public static void CreateTableFromLinq(List<Car> myCars)
    {
        IEnumerable<XElement> nodes = from car in myCars
                                      select new XElement("tbody",

                                       new XElement("td", car.Model),
                                       new XElement("td", car.Year),
                                   new XElement("tr",
                                       new XElement("td", car.Motor.Model),
                                       new XElement("td", car.Motor.HorsePower),
                                       new XElement("td", car.Motor.Displacement)));
        XElement rootNode = new("table", nodes); //create a root node to contain the query results
        rootNode.Save("CarsFromLinq.xhtml");
    }

}