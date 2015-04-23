package privacy.learning;

import learning.LabeledSample;
import learning.ParametricModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by alex on 3/5/15.
 */
public class LogisticModel implements ParametricModel {

    private static final java.lang.String DELIMITER = ",";
    private double[] _parameters;

    public LogisticModel(double[] parameters) {
        _parameters = parameters;
    }

    public LogisticModel(String serializedModel) {
        deserialize(serializedModel);
    }


    public static double sigmoid(double[] features, double[] parameters) {
        double dotProduct = 0;

        for(int i = 0; i < parameters.length; i++){
            dotProduct += parameters[i] * features[i];
        }

        return 1.0 / (1.0 + Math.exp(-dotProduct));
    }


    public double errorProjection(LabeledSample example) {
        double prediction = sigmoid(example.getFeatures(), _parameters);

        double error = (example.getLabel() + 1.0) / 2.0 - prediction;
        return error;
    }

    @Override
    public void update(double epsilon, List<LabeledSample> data) {

        for(int iteration = 0; iteration < 100; iteration++) {


            double[] gradient = new double[_parameters.length];

            for(int i = 0; i < gradient.length; i++){
                final int finalI = i;
                gradient[i] = data.stream().mapToDouble(sample -> errorProjection(sample) * sample.getFeatures()[finalI]).sum();
            }

            for(int d = 0; d < _parameters.length; d++){
                _parameters[d] += 0.07*(gradient[d] - 2.0*0.001*_parameters[d]);
            }

        }
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

    @Override
    public List<Double> label(List<LabeledSample> test) {
        List<Double> labels = new ArrayList<>();
        for(LabeledSample sample : test){
            labels.add(label(sample.getFeatures()));
        }

        return labels;
    }

    @Override
    public double label(double[] features) {
        double sigmoidValue = sigmoid(features, _parameters);
        return Math.round(sigmoidValue)*2.0 - 1.0;
    }

    public double[] getParameters() {
        return _parameters;
    }

    @Override
    public void addTerm(double[] term) {
        for (int i = 0; i < _parameters.length; i++) {
            _parameters[i] += term[i];
        }
    }
}
