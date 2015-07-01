package application;

import com.google.inject.Binder;
import com.google.inject.Module;
import experiment.ExperimentConfiguration;
import learning.ModelFactory;
import learning.models.LogisticModelFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by alex on 4/27/15.
 */
public class ExperimentModule implements Module {
    private final ExperimentConfiguration _configuration;
    private CountDownLatch _registrationLatch;

    public ExperimentModule(ExperimentConfiguration configuration, CountDownLatch registrationLatch) {
        _configuration = configuration;
        _registrationLatch = registrationLatch;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(ExperimentConfiguration.class).toInstance(_configuration);
        binder.bind(ModelFactory.class).to(LogisticModelFactory.class);
        binder.bind(CountDownLatch.class).toInstance(_registrationLatch);
    }
}
