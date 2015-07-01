package learning.metrics;

import learning.LabeledSample;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by alex on 3/17/15.
 */
public class PerformanceMetrics {

    public static double errorRate(List<LabeledSample> test, List<Double> predictions) {

        double wrong = 0.0;

        for(int i = 0; i < test.size(); i++){
            if(((int)test.get(i).getLabel()) != ((int) (predictions.get(i).doubleValue())*2.0-1.0) ){
                wrong++;
            }
        }

        double errorRate = wrong / (double) test.size();

        return errorRate;
    }

}
