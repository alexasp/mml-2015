using System;
using System.Collections.Generic;
using System.Linq;

namespace PreliminaryExperiments
{
    internal class LogisticModel
    {
        private double[] _parameters;

        public LogisticModel(double[] parameters)
        {
            _parameters = parameters;
        }

        public void Label(Dataset testSet)
        {
            foreach (var example in testSet.Samples)
            {
                double dotProduct = Dot(_parameters, example.Vector);
                double prediction = Logit(dotProduct);
                example.Label = prediction < 0.5 ? -1 : 1;
            }
        }
        private double Logit(double dotProduct)
        {
            return 1 / (1 + Math.Pow(Math.E, -dotProduct));
        }

        private double Dot(IEnumerable<double> parameters, IList<double> vector)
        {
            return parameters.Select((t, i) => t*vector[i]).Sum();
        }

 
    }
}