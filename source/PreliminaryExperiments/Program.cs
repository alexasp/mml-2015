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

        // generates an unbounded number of data points of specified dimension
        public static IEnumerable<double[]> GenerateData(int dimensions)
        {
            var RNG = new Random();
            while (true)
            {
                var vector = new double[dimensions];
                foreach (var index in Enumerable.Range(0, vector.Length))
                    vector[index] = 2.0 * RNG.NextDouble() - 1.0;

                yield return vector;
            }
        }

        public static double[] NearestCenter(double[] vector, double[][] centers)
        {
            // written in LINQ to "prove it can be done". easier to see that it is functional, too
            return centers.Aggregate(centers[0], (old, cur) => (vector.Select((v, i) => v - old[i])
                                                                      .Select(diff => diff * diff)
                                                                      .Sum() >
                                                                vector.Select((v, i) => v - cur[i])
                                                                      .Select(diff => diff * diff)
                                                                      .Sum()) ? cur : old);
        }

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

            var dimensions = 8;
            var records = 10000;
            var sourcedata = GenerateData(dimensions).Take(records).ToArray().AsQueryable();
            var securedata = new PINQueryable<double[]>(sourcedata, null);
            var k = 3;
            var centers = GenerateData(dimensions).Take(k).ToArray();
            foreach (var iteration in Enumerable.Range(0, iterations))
                MachineLearning.Program.kMeansStep(securedata, centers, 0.1);


            var labeled = securedata.Select(x => new MachineLearning.Program.Example(x, NearestCenter(x, centers) == centers[0] ? 1.0 : -1.0));

            var parameters = new double[dimensions];
            for (int i = 0; i < parameters.Length; i++)
            {
                parameters[i] = 2.0*random.NextDouble() - 1.0;
            }

            
            for (int iteration = 0; iteration < iterations; iteration++)
            {
                parameters = MachineLearning.Program.LogisticStep(labeled, parameters, 0.1);

                var logisticerror = labeled.NoisyAverage(0.1, x => x.Label * x.Vector.Select((v, i) => v * parameters[i]).Sum() < 0.0 ? 1.0 : 0.0);
                Console.WriteLine("logistic error rate:\t\t{0:F4}", logisticerror);
//
//                var model = new LogisticModel(parameters);
//
//                model.Label(testSet);
//                double errorRate = PerformanceMetrics.ErrorRate(labels, testSet);
//
//                Console.Out.WriteLine("Accuracy is " + errorRate);
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
