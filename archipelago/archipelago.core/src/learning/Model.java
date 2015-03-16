package learning;

import privacy.NoisyQueryable;

/**
 * Created by alex on 3/9/15.
 */
public interface Model {
    void update(double epsilon, NoisyQueryable<LabeledExample> queryable);
}
