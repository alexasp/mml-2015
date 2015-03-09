package privacy.learning;

import learning.LabeledExample;
import learning.Model;
import learning.models.LogisticModel;
import privacy.NoisyQueryable;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by alex on 3/5/15.
 */
public class DifferentialLogisticModel implements Model {

    private LogisticModel _logisticModel;

    public void step(double epsilon, NoisyQueryable queryable) {
        throw new UnsupportedOperationException();
    }

    public static Double errorProjection(LabeledExample example) {
        return LogisticModel.errorProjection(example);
    }


}
