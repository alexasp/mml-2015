using System.Collections.Generic;

namespace PreliminaryExperiments
{
    internal class PerformanceMetrics
    {
        
        public static double ErrorRate(List<double> trueLabels, Dataset labelledSet)
        {
            int correctLabels=0;
            for (int i = 0; i < labelledSet.Samples.Count; i++)
            {
                double label = trueLabels[i];
                double guessedLabel = labelledSet.Samples[i].Label;
                if (label.Equals(guessedLabel))
                {
                    correctLabels++;
                }
            }
            return correctLabels / labelledSet.Samples.Count;
    
        }
    }
}