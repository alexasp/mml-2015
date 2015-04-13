package privacy.learning;

import learning.IQueryable;
import learning.LabeledSample;
import learning.Model;
import learning.models.LogisticModel;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by alex on 3/5/15.
 */
public class DifferentialLogisticModel implements Model {

    private LogisticModel _logisticModel;

    public DifferentialLogisticModel(LogisticModel logisticModel) {
        _logisticModel = logisticModel;
    }


    public Double errorProjection(LabeledSample example) {
        double prediction = sigmoid(example.getFeatures(), _logisticModel.getParameters());
        double error = (example.getLabel() + 1.0) / 2.0 - prediction;
        return error;
    }

    public static double sigmoid(double[] features, double[] parameters) {
        double dotProduct = IntStream.range(0, features.length)
                .mapToDouble(i -> features[i] * parameters[i])
                .sum();

        return 1.0 / (1.0 + Math.exp(-dotProduct));
    }


    @Override
    public void update(double epsilon, IQueryable<LabeledSample> queryable) {
        IQueryable<Double> errors = queryable.project(example -> errorProjection( example));
        double[] parameters = _logisticModel.getParameters();
        double[] gradient = IntStream.range(0, _logisticModel.getDimensionality())
                .mapToDouble(i -> errors.sum(epsilon, error -> error * parameters[i]))
                .toArray();

        _logisticModel.gradientUpdate(gradient);
    }

    @Override
    public void deserialize(String modelString) {
        _logisticModel.deserialize(modelString);
    }

    @Override
    public String serialize() {
        return _logisticModel.serialize();
    }

    @Override
    public List<Double> label(List<LabeledSample> test) {
        return test.stream()
                .mapToDouble(sample -> label(sample.getFeatures()))
                .boxed()
                .collect(Collectors.toList());
    }

    @Override
    public double label(double[] features) {
        return Math.round(LogisticModel.sigmoid(features, _logisticModel.getParameters()))*2.0 - 1.0;
    }

}
