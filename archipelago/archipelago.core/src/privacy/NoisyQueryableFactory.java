package privacy;

import com.google.inject.Inject;
import learning.IQueryableFactory;
import privacy.math.RandomGenerator;

import java.util.List;

/**
 * Created by aspis on 25.03.2015.
 */
public class NoisyQueryableFactory implements IQueryableFactory{
    private RandomGenerator _randomGenerator;

    @Inject
    public NoisyQueryableFactory(RandomGenerator randomGenerator) {
        _randomGenerator = randomGenerator;
    }

    public <T> NoisyQueryable<T> getQueryable(Budget budget, List<T> data) {
        return NoisyQueryable.getQueryable(budget, data, _randomGenerator);
    }
}
