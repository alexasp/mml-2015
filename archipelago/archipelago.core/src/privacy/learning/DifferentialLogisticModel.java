package privacy.learning;

import learning.LabeledSample;
import learning.Model;
import learning.models.LogisticModel;
import privacy.NoisyQueryable;

import java.util.stream.IntStream;

/**
 * Created by alex on 3/5/15.
 */
public class DifferentialLogisticModel implements Model {

    private LogisticModel _logisticModel;

    public DifferentialLogisticModel(LogisticModel logisticModel) {
        _logisticModel = logisticModel;
    }

    private double gradientUpdate(NoisyQueryable<Double> errors, double parameter, double epsilon) {
        return errors.sum(epsilon, error -> error * parameter);
    }

    public Double errorProjection(LabeledSample example) {
        return _logisticModel.errorProjection(example);
    }


    @Override
    public void update(double epsilon, NoisyQueryable<LabeledSample> queryable) {
        NoisyQueryable<Double> errors = queryable.project(example -> errorProjection( example));
        double[] parameters = _logisticModel.getParameters();
        double[] gradient = IntStream.range(0, _logisticModel.getDimensionality())
                .mapToDouble(i -> gradientUpdate(errors, parameters[i], epsilon))
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

}
