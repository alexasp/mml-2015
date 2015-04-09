package learning;

import learning.IQueryable;
import learning.IQueryableFactory;
import learning.Queryable;
import privacy.Budget;
import privacy.NoisyQueryable;

import java.util.List;

/**
 * Created by alex on 4/9/15.
 */
public class QueryableFactory implements IQueryableFactory{
    @Override
    public <T> IQueryable<T> getQueryable(Budget budget, List<T> data) {
        return new Queryable(data);
    }
}
