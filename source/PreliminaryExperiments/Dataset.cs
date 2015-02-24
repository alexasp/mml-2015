using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections.Generic;
using System.Globalization;
using Microsoft.VisualBasic.FileIO;

namespace PreliminaryExperiments
{
    class Dataset
    {

        public Dataset(List<MachineLearning.Program.Example> examples)
        {
            Samples = examples;
        }

        public Dataset ExtractSample(double rate)
        {
            var newExamples = new List<MachineLearning.Program.Example>();
            var random = new Random((int) DateTime.Now.Ticks);

            foreach (var example in Samples)
            {
                if (random.NextDouble() < rate)
                {
                    newExamples.Add(example);
                }
            }

            foreach (var newExample in newExamples)
            {
                Samples.Remove(newExample);
            }

            return new Dataset(newExamples);
        }

        public List<MachineLearning.Program.Example> Samples { get; private set; }


        public static Dataset FromCsvFile(string path, int labelPos, string delimiter = ",")
        {
            var examples = new List<MachineLearning.Program.Example>();
            var parser = new TextFieldParser(path) {TextFieldType = FieldType.Delimited};

            parser.SetDelimiters(delimiter);
            while (!parser.EndOfData)
            {
                String[] fields = parser.ReadFields();
                if (fields == null) { throw new MissingFieldException("Reading fields from data file did not return any fields."); }

                double label = fields[labelPos] == "M" ? 1 : -1;
                var features = new double[fields.Length-2];

                for (int index = 1; index < fields.Length; index++)
                {
                    var field = fields[index];
                    if (index != labelPos)
                    {
                        features[index] = Double.Parse(field, CultureInfo.InvariantCulture);
                    }
                }

                examples.Add(new MachineLearning.Program.Example(features, label));
            } 

            return new Dataset(examples);
        }


        public List<double> ClearLabels()
        {
            var labels = new List<double>();

            foreach (var example in Samples)
            {
                labels.Add(example.Label);
                example.Label = 0;
            }

            return labels;
        }
    }
}
