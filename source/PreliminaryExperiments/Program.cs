﻿using System;
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
            var dataset = Dataset.FromCsvFile(@"wdbc.data", 1);

            const int iterations = 5;

            var trainSet = dataset.ExtractSample(0.8);
            
            Dataset testSet = dataset;
            var labels = testSet.ClearLabels();

            var agent = new PINQAgentBudget(35*iterations);
            var pinqQueryable = new PINQueryable<MachineLearning.Program.Example>(trainSet.Samples.AsQueryable(), agent);
            var random = new Random();

            var parameters = new double[30];
            for (int i = 0; i < parameters.Length; i++)
            {
                parameters[i] = random.NextDouble()*2 - 1;
            }

            
            for (int iteration = 0; iteration < iterations; iteration++)
            {
                parameters = MachineLearning.Program.LogisticStep(pinqQueryable, parameters,1);

                var model = new LogisticModel(parameters);

                model.Label(testSet);
                double errorRate = PerformanceMetrics.ErrorRate(labels, testSet);

                Console.Out.WriteLine("Accuracy is " + errorRate);
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
            Console.Read();
        }
    }
}
