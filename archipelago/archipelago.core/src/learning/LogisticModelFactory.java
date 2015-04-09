package learning;

import learning.models.LogisticModel;
import privacy.learning.DifferentialLogisticModel;

/**
 * Created by alex on 3/23/15.
 */
public class LogisticModelFactory implements ModelFactory{

    @Override
    public Model getModel(double[] parameters) {
        return new LogisticModel(parameters);
    }

    @Override
    public Model getModel(int size) {
        return new LogisticModel(new double[size]);
    }

    @Override
    public Model getModel(String serializedModel) {
        return new LogisticModel(serializedModel);
    }
}
