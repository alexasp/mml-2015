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
        private List<MachineLearning.Program.Example> examples;

        public Dataset()
        {
            examples = new List<MachineLearning.Program.Example>();
        }
        public List<MachineLearning.Program.Example> parseCSVFileToExample(string path, int labelPos)
        {
            //var examples = new List<MachineLearning.Program.Example>();

            var parser = new TextFieldParser(path) {TextFieldType = FieldType.Delimited};
            parser.SetDelimiters(",");
            while (!parser.EndOfData)
            {
                String[] fields = parser.ReadFields();
                double label = Double.Parse(fields[labelPos]);
                double[] features = new double[fields.Length-1];

                for(int i = 0; i < fields.Length; i++){

                }
            } 
            return null;
        }
        
    }
}
