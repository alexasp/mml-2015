package learning.models;

import com.google.inject.Inject;
import experiment.ExperimentConfiguration;
import learning.ParametricModel;
import learning.ModelFactory;
import privacy.learning.LogisticModel;

/**
 * Created by alex on 4/9/15.
 */
public class LogisticModelFactory implements ModelFactory {

    private final ExperimentConfiguration _configuration;

    @Inject
    public LogisticModelFactory(ExperimentConfiguration configuration) {
        _configuration = configuration;
    }

    @Override
    public ParametricModel getModel(double[] parameters) {
        return new LogisticModel(parameters, _configuration.regularization);
    }

    @Override
    public ParametricModel getModel(int size) {
        return new LogisticModel(new double[size], _configuration.regularization);
    }

    @Override
    public ParametricModel getModel(String serializedModel) {
        return new LogisticModel(serializedModel, _configuration.regularization);
    }
}
