package application;

import learning.QueryableFactory;
import com.google.inject.AbstractModule;
import learning.IQueryableFactory;
import learning.ModelFactory;
import learning.models.PrivateLogisticModelFactory;

/**
 * Created by aspis on 25.03.2015.
 */
public class AppInjector extends AbstractModule {
    @Override
    protected void configure() {
        bind(ModelFactory.class).to(PrivateLogisticModelFactory.class);
        bind(IQueryableFactory.class).to(QueryableFactory.class);
    }
}
