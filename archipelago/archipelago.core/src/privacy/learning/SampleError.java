package privacy.learning;

/**
 * Created by alex on 4/13/15.
 */
public class SampleError {

    public final double error;
    public final double[] features;

    public SampleError(double[] features, double error) {
        this.features = features;
        this.error = error;
    }
}
