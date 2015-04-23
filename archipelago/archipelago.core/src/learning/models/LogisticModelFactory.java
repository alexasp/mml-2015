package learning.models;

import learning.ParametricModel;
import learning.ModelFactory;
import privacy.learning.LogisticModel;

/**
 * Created by alex on 4/9/15.
 */
public class LogisticModelFactory implements ModelFactory {
    @Override
    public ParametricModel getModel(double[] parameters) {
        return new LogisticModel(parameters);
    }

    @Override
    public ParametricModel getModel(int size) {
        return new LogisticModel(new double[size]);
    }

    @Override
    public ParametricModel getModel(String serializedModel) {
        return new LogisticModel(serializedModel);
    }
}
