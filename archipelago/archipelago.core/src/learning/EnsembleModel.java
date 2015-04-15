package learning;

import privacy.NoisyQueryable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * Created by alex on 3/9/15.
 */
public class EnsembleModel implements Model{

    private final ArrayList<Model> _ensemble;

    public EnsembleModel() {
        _ensemble = new ArrayList<>();
    }

    public void add(Model model) {
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
        ArrayList<Model> ensembleCopy = new ArrayList<>(_ensemble);
        return test.stream()
                .mapToDouble(sample ->
                        Math.round(ensembleCopy.stream().mapToDouble(model -> model.label(sample.getFeatures())).average().getAsDouble()))
                .boxed().collect(Collectors.toList());
    }

    @Override
    public double label(double[] test) {
        throw new UnsupportedOperationException();
    }

    public ArrayList<Model> getModels() {
        return _ensemble;
    }
}
