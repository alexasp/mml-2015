package learning;

import privacy.NoisyQueryable;

import java.io.Serializable;

/**
 * Created by alex on 3/9/15.
 */
public interface Model {
    void update(double epsilon, NoisyQueryable<LabeledSample> queryable);

    void deserialize(String modelString);

    String serialize();
}
