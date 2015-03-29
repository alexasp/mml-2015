package learning;

import learning.models.LogisticModel;

/**
 * Created by alex on 3/23/15.
 */
public class ModelFactory {
    public LogisticModel getLogisticModel(double[] parameters) {
        return new LogisticModel(parameters);
    }
}
