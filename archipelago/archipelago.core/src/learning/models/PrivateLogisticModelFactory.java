package learning.models;

import learning.Model;
import learning.ModelFactory;
import privacy.learning.DifferentialLogisticModel;

/**
 * Created by alex on 4/9/15.
 */
public class PrivateLogisticModelFactory implements ModelFactory {
    @Override
    public Model getModel(double[] parameters) {
        return new DifferentialLogisticModel(parameters);
    }

    @Override
    public Model getModel(int size) {
        return new DifferentialLogisticModel(new double[size]);
    }

    @Override
    public Model getModel(String serializedModel) {
        return new DifferentialLogisticModel(serializedModel);
    }
}
