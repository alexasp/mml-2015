package learning.models;

import learning.LabeledExample;
import learning.Model;

import java.util.stream.IntStream;

/**
 * Created by alex on 3/9/15.
 */
public class LogisticModel implements Model {
    private double[] _parameters;

    public double[] getParameters() {
        return _parameters;
    }

    public Double errorProjection(LabeledExample example) {
        double error = (example.getLabel() + 1.0) / 2.0 - sigmoid(example.getFeatures());
        return error;
    }

    private double sigmoid(double[] features) {
        double dotProduct = IntStream.range(0, features.length)
                .parallel()
                .mapToDouble(i -> features[i] * _parameters[i])
                .sum();

        return 1.0 / (1 + Math.exp(-dotProduct));
    }
}
