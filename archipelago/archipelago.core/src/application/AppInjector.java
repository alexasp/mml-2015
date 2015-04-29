package application;

import learning.QueryableFactory;
import com.google.inject.AbstractModule;
import learning.IQueryableFactory;
import learning.ModelFactory;
import learning.models.LogisticModelFactory;

/**
 * Created by aspis on 25.03.2015.
 */
public class AppInjector extends AbstractModule {
    @Override
    protected void configure() {

        bind(IQueryableFactory.class).to(QueryableFactory.class);
    }
}
