package application;

import com.google.inject.AbstractModule;
import communication.Environment;
import learning.LogisticModelFactory;
import learning.ModelFactory;
import learning.models.LogisticModel;
import learning.models.PrivateLogisticModelFactory;

/**
 * Created by aspis on 25.03.2015.
 */
public class AppInjector extends AbstractModule {
    @Override
    protected void configure() {
        bind(ModelFactory.class).to(PrivateLogisticModelFactory.class);
    }
}
