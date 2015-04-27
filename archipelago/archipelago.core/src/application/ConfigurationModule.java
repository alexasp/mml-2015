package application;

import com.google.inject.Binder;
import com.google.inject.Module;
import experiment.ExperimentConfiguration;

/**
 * Created by alex on 4/27/15.
 */
public class ConfigurationModule implements Module {
    private final ExperimentConfiguration _configuration;

    public ConfigurationModule(ExperimentConfiguration configuration) {
        _configuration = configuration;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(ExperimentConfiguration.class).toInstance(_configuration);
    }
}
