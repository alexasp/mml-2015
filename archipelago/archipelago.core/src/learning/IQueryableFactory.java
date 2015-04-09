package learning;

import privacy.Budget;
import privacy.NoisyQueryable;

import java.util.List;

/**
 * Created by alex on 4/9/15.
 */
public interface IQueryableFactory {

    public <T> IQueryable<T> getQueryable(Budget budget, List<T> data);

}
