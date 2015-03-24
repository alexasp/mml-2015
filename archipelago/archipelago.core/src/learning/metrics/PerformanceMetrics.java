package learning.metrics;

import learning.LabeledSample;
import privacy.NoisyQueryable;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Created by alex on 3/17/15.
 */
public class PerformanceMetrics {
    public double errorRate(NoisyQueryable<LabeledSample> test, List<Double> predictions, double cost) {
        BiFunction<LabeledSample, Integer, Double> errorFunction =
                (sample, index) -> (sample.getLabel() + 1.0) / 2.0 - predictions.get(index);

        return test.noisyAverage(cost, errorFunction);
    }

}
