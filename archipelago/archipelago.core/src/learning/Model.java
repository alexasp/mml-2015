package learning;

import privacy.NoisyQueryable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 3/9/15.
 */
public interface Model {
    void update(double epsilon, IQueryable<LabeledSample> queryable);

    void deserialize(String modelString);

    String serialize();

    List<Double> label(List<LabeledSample> test);

    double label(double[] test);
}
