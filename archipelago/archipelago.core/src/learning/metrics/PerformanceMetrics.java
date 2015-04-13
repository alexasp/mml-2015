package learning.metrics;

import learning.LabeledSample;
import privacy.NoisyQueryable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by alex on 3/17/15.
 */
public class PerformanceMetrics {
    public double errorRate(List<LabeledSample> test, List<Double> predictions, double cost) {

        double correct = 0.0;

        for(int i = 0; i < test.size(); i++){
            if(((int)test.get(i).getLabel()) == ((int) predictions.get(i).doubleValue()) ){
                correct++;
            }
        }
        double errorRate = (test.size() - correct) / (double) test.size();

        return errorRate;
    }

}
