package learning;

import privacy.NoisyQueryable;

import java.util.ArrayList;

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
    }

    @Override
    public void update(double epsilon, IQueryable<LabeledSample> queryable) {

    }

    @Override
    public void deserialize(String modelString) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String serialize() {
        throw new UnsupportedOperationException();
    }

    public ArrayList<Model> getModels() {
        return _ensemble;
    }
}
