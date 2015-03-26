package privacy;

import privacy.math.RandomGenerator;

import java.util.List;

/**
 * Created by aspis on 25.03.2015.
 */
public class NoisyQueryableFactory {
    private RandomGenerator _randomGenerator;

    public <T> NoisyQueryable<T> getQueryable(Budget budget, List<T> data) {
        return NoisyQueryable.getQueryable(budget, data, _randomGenerator);
    }
}
