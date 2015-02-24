using System;
using System.Collections.Generic;
using System.Globalization;
using Microsoft.VisualBasic.FileIO;

namespace PreliminaryExperiments
{
    public class DataLoader
    {
        public static List<MachineLearning.Program.Example> CsvToExamples(string wdbcData)
        {
            var examples = new List<MachineLearning.Program.Example>();

            var parser = new TextFieldParser(wdbcData) {TextFieldType = FieldType.Delimited};
            parser.SetDelimiters(",");
            while (!parser.EndOfData)
            {
                const int offset = 2;

                string[] fields = parser.ReadFields();
                var vector = new double[fields.Length-offset];
                
                string label = fields[1];

                double numericLabel = label == "M" ? 1.0 : -1.0;

                for (int i = offset; i < fields.Length; i++)
                {
                    vector[i - offset] = double.Parse(fields[i], CultureInfo.InvariantCulture);
                }

                examples.Add(new MachineLearning.Program.Example(vector, numericLabel));
            }
            parser.Close();

            return examples;
        }

    }
}