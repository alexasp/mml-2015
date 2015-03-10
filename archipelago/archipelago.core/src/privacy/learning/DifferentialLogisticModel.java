package privacy.learning;

import learning.LabeledExample;
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

    public void step(double epsilon, NoisyQueryable<LabeledExample> queryable) {
        NoisyQueryable<Double> errors = queryable.project(example -> errorProjection(example));
        double[] parameters = _logisticModel.getParameters();
        double[] gradient = IntStream.range(0, _logisticModel.getDimensionality())
                .mapToDouble(i -> gradientUpdate(errors, parameters[i], epsilon))
                .toArray();
        _logisticModel.gradientUpdate(gradient);
    }

    private double gradientUpdate(NoisyQueryable<Double> errors, double parameter, double epsilon) {
        return errors.project(error -> error * parameter).sum(epsilon);
    }

    public Double errorProjection(LabeledExample example) {
        return _logisticModel.errorProjection(example);
    }


}
