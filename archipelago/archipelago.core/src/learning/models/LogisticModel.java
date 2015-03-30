package learning.models;

import learning.LabeledSample;
import learning.Model;
import privacy.NoisyQueryable;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by alex on 3/9/15.
 */
public class LogisticModel implements Model {
    private double[] _parameters;

    public LogisticModel(double[] parameters) {
        if(parameters.length == 0){ throw new IllegalArgumentException("Parameter vector must have length greater than 0."); }
        _parameters = Arrays.copyOf(parameters, parameters.length);
    }

    public double[] getParameters() {
        return _parameters;
    }

    //TODO: use function from PerformanceMetrics?
    public Double errorProjection(LabeledSample example) {
        double error = (example.getLabel() + 1.0) / 2.0 - sigmoid(example.getFeatures(), _parameters);
        return error;
    }

    private static double sigmoid(double[] features, double[] parameters) {
        double dotProduct = IntStream.range(0, features.length)
                .parallel()
                .mapToDouble(i -> features[i] * parameters[i])
                .sum();

        return 1.0 / (1 + Math.exp(-dotProduct));
    }

    public void gradientUpdate(double[] gradient) {
        _parameters = IntStream.range(0, _parameters.length)
                .mapToDouble(i -> _parameters[i] + gradient[i])
                .toArray();
    }

    public int getDimensionality() {
        return _parameters.length;
    }

    @Override
    public void update(double epsilon, NoisyQueryable queryable) {
        throw new UnsupportedOperationException();
    }
}
