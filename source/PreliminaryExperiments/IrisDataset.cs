using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PreliminaryExperiments
{
    class IrisDataset
    {
          public static Dataset FromCsvFile(string path, int labelPos, string delimiter = ",")
        {
            var examples = new List<MachineLearning.Program.Example>();
            var parser = new TextFieldParser(path) {TextFieldType = FieldType.Delimited};

            parser.SetDelimiters(delimiter);
            while (!parser.EndOfData)
            {
                String[] fields = parser.ReadFields();
                if (fields == null) { throw new MissingFieldException("Reading fields from data file did not return any fields."); }

                double label = fields[labelPos] == "Iris-setosa" ? 1 : -1;
                var features = new double[fields.Length-1];

                int offset = 0;
                for (int index = 0; index < fields.Length-2; index++)
                {
                    if (index == labelPos) { offset = 1; }
                    var field = fields[index + offset];

                    features[index] = Double.Parse(field, CultureInfo.InvariantCulture);
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
}
