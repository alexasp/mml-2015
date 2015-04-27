package learning.metrics;

import learning.LabeledSample;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 3/17/15.
 */
public class PerformanceMetrics {

    LinkedHashMap<String,Integer> _confusionMatrix;
    public double errorRate(List<LabeledSample> test, List<Double> predictions) {

        double wrong = 0.0;

        for(int i = 0; i < test.size(); i++){
            if(((int)test.get(i).getLabel()) != ((int) predictions.get(i).doubleValue()) ){
                wrong++;
            }
        }

        double errorRate = wrong / (double) test.size();

        return errorRate;
    }

    public Map<String,Integer> confusionMatrix(List<LabeledSample> test, List<Double> predictions) {
        return _confusionMatrix;
    }






    public void printConfusionMatrix(Map<String,Integer> confusionMatrix){

        int correct = confusionMatrix.get("TP")+confusionMatrix.get("TN");
        int incorrect= confusionMatrix.get("FP") + confusionMatrix.get("FN");
        double percentageCorrect = (correct/(correct+incorrect))*100;

        System.out.println("Correctly classified instances: " + correct + " , " + percentageCorrect +"%");
        System.out.println("Incorrectly classified instances: " +incorrect + " , " + (100-percentageCorrect) +"%");

        System.out.println("== Confusion Matrix ==");
        System.out.println("T F <-- classified as" );
        System.out.println(confusionMatrix.get("TP") + " " +confusionMatrix.get("FN"));
        System.out.println(confusionMatrix.get("FP") + " " +confusionMatrix.get("TN"));
    }

}
