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

    public static final String DELIMITER = ",";

    public LogisticModel(double[] parameters) {
        if(parameters.length == 0){ throw new IllegalArgumentException("Parameter vector must have length greater than 0."); }
        _parameters = Arrays.copyOf(parameters, parameters.length);
    }

    public LogisticModel(String modelString) {
        deserialize(modelString);
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

    @Override
    public void deserialize(String modelString) {
        String[] parts = modelString.split(DELIMITER);

        _parameters = IntStream.range(0, parts.length)
                .mapToDouble(i -> Double.parseDouble(parts[i]))
                .toArray();
    }

    @Override
    public String serialize() {
        StringBuilder builder = new StringBuilder();
        builder.append(Double.toString(_parameters[0]));

        for(int i = 1; i < _parameters.length; i++){
            builder.append(",");
            builder.append(Double.toString(_parameters[i]));
        }

        return builder.toString();
    }
}
