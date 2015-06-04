package privacy.learning;

import experiment.DataLoader;
import learning.LabeledSample;
import learning.ParametricModel;
import learning.metrics.PerformanceMetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by alex on 3/5/15.
 */
public class LogisticModel implements ParametricModel {

    private static final java.lang.String DELIMITER = ",";
    private double[] _parameters;
    private double _regularization;

    public LogisticModel(double[] parameters, double regularization) {
        _parameters = parameters;
        _regularization = regularization;
    }

    public LogisticModel(String serializedModel, double regularization) {
        deserialize(serializedModel);
        _regularization = regularization;
    }


    public static double sigmoid(double[] features, double[] parameters) {
        double dotProduct = 0;

        for(int i = 0; i < parameters.length; i++){
            dotProduct += parameters[i] * features[i];
        }

        return 1.0 / (1.0 + Math.exp(-dotProduct));
    }


    public double errorProjection(LabeledSample example, double[] parameters) {
        double prediction = sigmoid(example.getFeatures(), parameters);

        double error = (example.getLabel() + 1.0) / 2.0 - prediction;
        return error;
    }

    @Override
    public void update(double epsilon, List<LabeledSample> data) {
        List<Double> alphas = IntStream.range(-6, 7).mapToDouble(i -> Math.pow(2, i)).boxed().collect(Collectors.toList());
        double best_alpha = alphas.get(0);
        double best_error = 1.0;
        List<List<LabeledSample>> folds = DataLoader.partition(3, data);

        for (Double alpha : alphas) {

            double errorRate = 0.0;
            for (int i = 0; i < folds.size(); i++) {
                List<LabeledSample> test = folds.get(i);
                List<LabeledSample> train = DataLoader.mergeExcept(folds, i);
                double[] parameters = fitModel(train, alpha, 1);

                errorRate += PerformanceMetrics.errorRate(test, label(test, parameters))/(double)folds.size();
            }
            if(errorRate < best_error){
                best_error = errorRate;
                best_alpha = alpha;
            }
        }

        _parameters = fitModel(data, best_alpha, 100);
    }

    


    private double[] fitModel(List<LabeledSample> train, double eta, int epochs) {
        double[] parameters = Arrays.copyOf(_parameters, _parameters.length);

        Collections.shuffle(train);

        for (int epoch = 0; epoch < epochs; epoch++) {

            double alpha = eta / (1 + _regularization * eta * epoch);
            ArrayList<Double> logConditionalLikelihoods = new ArrayList<>();
            logConditionalLikelihoods.add(calculateLcl(train, parameters, _regularization));

            for (LabeledSample labeledSample : train) {
                for (int i = 0; i < parameters.length - 1; i++) {
                    parameters[i] += alpha * (errorProjection(labeledSample, parameters)*labeledSample.getFeatures()[i] - 2 * _regularization * parameters[i]);
                }
                parameters[parameters.length - 1] += alpha * (errorProjection(labeledSample, parameters)*labeledSample.getFeatures()[parameters.length - 1]);

                logConditionalLikelihoods.add(calculateLcl(train, parameters, _regularization));

                if(Math.abs(logConditionalLikelihoods.get(logConditionalLikelihoods.size() - 1) - logConditionalLikelihoods.get(logConditionalLikelihoods.size() - 2)) < 0.01){
                    break;
                }
                else if(epoch == 99){
                    assert Boolean.TRUE;
                }
                else{
                    assert Boolean.TRUE;
                }
            }
        }

        return parameters;
    }

    private Double calculateLcl(List<LabeledSample> train, double[] parameters, double regularization) {
        double logConditionalLikelihood = 0.0;
        for (LabeledSample labeledSample : train) {
            double probability = sigmoid(labeledSample.getFeatures(), parameters);
            double y = labeledSample.getLabel() == 1.0 ? 1.0 : 0.0;
            logConditionalLikelihood += probability * y + (1.0-probability) * (1 - y);
        }
        logConditionalLikelihood -= regularization * IntStream.range(0, parameters.length).mapToDouble(i -> parameters[i]*parameters[i]).sum();
        return logConditionalLikelihood;
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

    public List<Double> label(List<LabeledSample> test, double[] parameters) {
        List<Double> labels = new ArrayList<>();
        for(LabeledSample sample : test){
            labels.add(label(sample.getFeatures(), parameters));
        }

        return labels;
    }

    @Override
    public double label(double[] features) {
        return label(features, _parameters);
    }

    private double label(double[] features, double[] parameters) {
        double sigmoidValue = sigmoid(features, parameters);
        return Math.round(sigmoidValue);
    }

    @Override
    public double label(double[] features, double threshold) {
        double sigmoidValue = sigmoid(features, _parameters);
        return sigmoidValue > threshold ? 1.0 : 0.0;
    }

    @Override
    public List<Double> label(List<LabeledSample> test, double threshold) {
        throw new UnsupportedOperationException();
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
