package learning.metrics;

import learning.LabeledSample;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by peterfh on 27.04.2015.
 */
public class ConfusionMatrix {

    public Map<String,Integer> _confusionMatrix;
    private double _threshold;
    
    public ConfusionMatrix(List<LabeledSample> test, List<Double> predictions,double threshold){
        _threshold=threshold;
        _confusionMatrix = new LinkedHashMap<String,Integer>();
        _confusionMatrix.put("TP",0);
        _confusionMatrix.put("FP",0);
        _confusionMatrix.put("TN",0);
        _confusionMatrix.put("FN",0);

        for(int i = 0; i < test.size(); i++) {
            if (((int) test.get(i).getLabel()) == 1 && ((int) predictions.get(i).doubleValue() == 1)) {
                _confusionMatrix.put("TP", _confusionMatrix.get("TP") + 1);
            } else if (((int) test.get(i).getLabel()) == -1 && ((int) predictions.get(i).doubleValue() == 1)) {
                _confusionMatrix.put("FP", _confusionMatrix.get("FP") + 1);
            } else if (((int) test.get(i).getLabel()) == -1 && ((int) predictions.get(i).doubleValue() == 0)) {
                _confusionMatrix.put("TN", _confusionMatrix.get("TN") + 1);
            } else if (((int) test.get(i).getLabel()) == 1 && ((int) predictions.get(i).doubleValue() == 0)) {
                _confusionMatrix.put("FN", _confusionMatrix.get("FN") + 1);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    public double getThreshold() {
        return _threshold;
    }

    public Double getSensitivity(){
        double fp = _confusionMatrix.get("TP");
        double div = (_confusionMatrix.get("TP")+_confusionMatrix.get("FN"));
        double fpr = (fp/div);
        return fpr;
    }
    public Double getFalsePositiveRate(){
        double fn = _confusionMatrix.get("FP");
        double div = (_confusionMatrix.get("TN")+_confusionMatrix.get("FP"));
        double fnr = fn/div;
        return fnr;
    }

    public Double getCorrectClassifiedPercentage(){
        double correct = _confusionMatrix.get("TP")+ _confusionMatrix.get("TN");
        double incorrect= _confusionMatrix.get("FP") + _confusionMatrix.get("FN");
        double percentageCorrect = (correct/(correct+incorrect))*100;
        return  percentageCorrect;
    }
    public int getTP(){
        return _confusionMatrix.get("TP");
    }
    public int getFP(){
        return _confusionMatrix.get("FP");
    }
    public int getFN(){
        return _confusionMatrix.get("FN");
    }
    public int getTN(){
        return _confusionMatrix.get("TN");
    }

    public void printConfusionMatrix(){

        double correct = _confusionMatrix.get("TP")+_confusionMatrix.get("TN");
        double incorrect= _confusionMatrix.get("FP") + _confusionMatrix.get("FN");
        double percentageCorrect = (correct/(correct+incorrect))*100;

        System.out.println("Correctly classified instances: " + correct + " , " + percentageCorrect +"%");
        System.out.println("Incorrectly classified instances: " + incorrect + " , " + (100 - percentageCorrect) + "%");

        System.out.println("== Confusion Matrix ==");
        System.out.println("T F <-- classified as" );
        System.out.println(_confusionMatrix.get("TP") + " " +_confusionMatrix.get("FN"));
        System.out.println(_confusionMatrix.get("FP") + " " +_confusionMatrix.get("TN"));
    }


}
