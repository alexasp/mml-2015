package learning.models;

import learning.Model;
import learning.ModelFactory;
import privacy.learning.LogisticModel;

/**
 * Created by alex on 4/9/15.
 */
public class LogisticModelFactory implements ModelFactory {
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
