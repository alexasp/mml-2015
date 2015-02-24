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
        private readonly List<MachineLearning.Program.Example> _examples;

        public Dataset(List<MachineLearning.Program.Example> examples)
        {
            _examples = new List<MachineLearning.Program.Example>();
        }

        public List<MachineLearning.Program.Example> Examples {
            get { return new List<MachineLearning.Program.Example>(_examples); }
        }


        public static Dataset FromCsvFile(string path, int labelPos, string delimiter = ",")
        {
            var examples = new List<MachineLearning.Program.Example>();
            var parser = new TextFieldParser(path) {TextFieldType = FieldType.Delimited};

            parser.SetDelimiters(delimiter);
            while (!parser.EndOfData)
            {
                String[] fields = parser.ReadFields();
                if (fields == null) { throw new MissingFieldException("Reading fields from data file did not return any fields."); }

                double label = Double.Parse(fields[labelPos]);
                var features = new double[fields.Length-1];

                int counter = 0;
                foreach (var field in fields)
                {
                    if (counter != labelPos){ features[counter] = Double.Parse(field); }
                    counter++;
                }

                examples.Add(new MachineLearning.Program.Example(features, label));
            } 

            return new Dataset(examples);
        }
        
    }
}
