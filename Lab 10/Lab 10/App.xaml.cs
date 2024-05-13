using System.Configuration;
using System.Data;
using System.Runtime.InteropServices;
using System.Windows;

namespace Lab_10
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App : Application
    {
        public static List<Car> myCars = new List<Car>(){
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

        public static void Queries()
        {
            // query expression
            var result1 = from e in (from car in myCars
                                     where car.model == "A6"
                                     select new { engineType = (car.motor.model == "TDI" ? "diesel" : "petrol"), hppl = car.motor.horsePower / car.motor.displacement })
                          group e by e.engineType into eGroup
                          select new
                          {
                              engineType = eGroup.First().engineType,
                              avgHPPL = eGroup.Average(s => s.hppl),
                          } into newGroup
                          orderby newGroup.avgHPPL descending
                          select newGroup;


            Console.WriteLine("query expression syntax: ");
            foreach (var e in result1)
                Console.WriteLine(e.engineType + ": " + e.avgHPPL);


            // method-based query
            var result2 = myCars
               .Where(car => car.model == "A6")
               .Select(car => new
               {
                   engineType = car.motor.model == "TDI" ? "diesel" : "petrol",
                   hppl = car.motor.horsePower / car.motor.displacement
               })
               .GroupBy(car => car.engineType)
               .Select(element => new
               {
                   engineType = element.First().engineType,
                   avgHPPL = element.Average(car => car.hppl)
               })
               .OrderByDescending(e => e.avgHPPL);


            Console.WriteLine("\nmethod-based query syntax: ");
            foreach (var e in result1)
                Console.WriteLine(e.engineType + " " + e.avgHPPL);
        }
        public static void CreateAndRunDelegates()
        {
            Func<Car, Car, int> arg1 = CompareCarsByHorsepower;
            Predicate<Car> arg2 = CheckIfTDI;
            Action<Car> arg3 = ShowElement;

            myCars.Sort(new Comparison<Car>(arg1));
            myCars.FindAll(arg2).ForEach(arg3);
        }
        private static int CompareCarsByHorsepower(Car car1, Car car2)
        {
            return car1.motor.horsePower.CompareTo(car2.motor.horsePower);
        }
        private static bool CheckIfTDI(Car car)
        {
            return car.motor.model == "TDI";
        }
        private static void ShowElement(Car car)
        {
            MessageBox.Show(car.ToString(), "Car");
        }

        [DllImport("kernel32.dll", SetLastError = true)]
        [return: MarshalAs(UnmanagedType.Bool)]
        static extern bool AllocConsole();

        [DllImport("Kernel32", SetLastError = true)]
        public static extern void FreeConsole();

        public App()
        {
            AllocConsole();
            Queries();
            CreateAndRunDelegates();
            MainWindow mainWindow = new MainWindow(myCars);
            mainWindow.Show();
            //FreeConsole();
        }
    }

}
