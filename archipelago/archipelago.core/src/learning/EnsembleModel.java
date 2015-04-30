package learning;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alex on 3/9/15.
 */
public class EnsembleModel implements Model {

    private final ArrayList<ParametricModel> _ensemble;

    public EnsembleModel() {
        _ensemble = new ArrayList<>();
    }

    public void add(ParametricModel model) {
        _ensemble.add(model);
//        if(_ensemble.size() > 10)
//            _ensemble.remove(0);
    }

    @Override
    public void update(double epsilon, List<LabeledSample> data) {

    }

    @Override
    public void deserialize(String modelString) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String serialize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Double> label(List<LabeledSample> test) {
        ArrayList<ParametricModel> ensembleCopy = new ArrayList<>(_ensemble);
        return test.stream()
                .mapToDouble(sample ->
                        Math.round(ensembleCopy.stream().mapToDouble(model -> model.label(sample.getFeatures())).average().getAsDouble()))
                .boxed().collect(Collectors.toList());
    }

    @Override
    public double label(double[] test) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double label(double[] test, double threshold) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Double> label(List<LabeledSample> test, double threshold) {
        ArrayList<ParametricModel> ensembleCopy = new ArrayList<>(_ensemble);
        return test.stream()
                .mapToDouble(sample -> {
                    double averageLabel = ensembleCopy.stream().mapToDouble(model -> model.label(sample.getFeatures(), threshold)).average().getAsDouble();
                    return averageLabel > threshold ? 1.0 : 0.0;
                })
                .boxed().collect(Collectors.toList());
    }

    public ArrayList<ParametricModel> getModels() {
        return _ensemble;
    }
}
