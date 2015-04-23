package learning;

import privacy.NoisyQueryable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 3/9/15.
 */
public interface ParametricModel extends Model {
    double[] getParameters();

    void addTerm(double[] term);
}
