package learning;

import java.util.List;

/**
 * Created by alex on 4/23/15.
 */
public interface Model {

    void update(double epsilon, List<LabeledSample> data);

    void deserialize(String modelString);

    String serialize();

    List<Double> label(List<LabeledSample> test);

    double label(double[] test);

    List<Double> label(List<LabeledSample> test, double threshold);
}
