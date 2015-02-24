using System;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PINQ;

namespace PreliminaryExperiments
{
    class Program
    {
        static void Main(string[] args)
        {
            List<MachineLearning.Program.Example> dataset = DataLoader.CsvToExamples(@"wdbc.data");

            const int iterations = 10;

            var agent = new PINQAgentBudget(3.5*iterations);
            var pinqQueryable = new PINQueryable<MachineLearning.Program.Example>(dataset.AsQueryable(), agent);
            var random = new Random();

            var parameters = new double[30];
            for (int i = 0; i < parameters.Length; i++)
            {
                parameters[i] = random.NextDouble();
            }

            PrintEnumerable(parameters);

            
            for (int iteration = 0; iteration < iterations; iteration++)
            {
                parameters = MachineLearning.Program.LogisticStep(pinqQueryable, parameters, 0.1);
                PrintEnumerable(parameters);
            }
            
        }

        private static void PrintEnumerable(IEnumerable<double> parameters)
        {
            var builder = new StringBuilder();
            foreach (var parameter in parameters)
            {
                builder.Append(parameter + ",");
            }
            Console.WriteLine(builder.ToString());
        }
    }
}
