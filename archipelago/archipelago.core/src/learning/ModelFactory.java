package learning;

import learning.models.LogisticModel;
import privacy.learning.DifferentialLogisticModel;

/**
 * Created by alex on 3/23/15.
 */
public class ModelFactory {
    private LogisticModel getLogisticModel(double[] parameters) {
        return new LogisticModel(parameters);
    }

    private LogisticModel getLogisticModel(String modelString) {
        return new LogisticModel(modelString);
    }

    public DifferentialLogisticModel getPrivateLogisticModel(double[] parameters) {
        return new DifferentialLogisticModel(getLogisticModel(parameters));
    }

    public DifferentialLogisticModel getPrivateLogisticModel(String modelString) {
        return new DifferentialLogisticModel(getLogisticModel(modelString));
    }
}
