package privacy.math;

import org.apache.commons.math3.distribution.LaplaceDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

/**
 * Created by alex on 3/5/15.
 */
public class NoiseGenerator {
    public double fromLaplacian(double beta) {
        LaplaceDistribution distribution = new LaplaceDistribution(0, beta);
        return distribution.sample();
    }

    public double uniform(double lower, double upper) {
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        return distribution.sample();
    }
}
