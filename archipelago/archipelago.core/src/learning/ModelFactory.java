package learning;

import learning.models.LogisticModel;
import privacy.learning.DifferentialLogisticModel;

/**
 * Created by alex on 3/23/15.
 */
public class ModelFactory {
    public LogisticModel getLogisticModel(double[] parameters) {
        return new LogisticModel(parameters);
    }

    public DifferentialLogisticModel getPrivateLogisticModel(double[] parameters) {
        return new DifferentialLogisticModel(getLogisticModel(parameters));
    }
}
