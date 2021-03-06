package learning;

/**
 * Created by alex on 3/9/15.
 */
public class LabeledSample {
    private final double[] _features;
    private double _label;

    public LabeledSample(double label, double[] features){
        _label = label;
        _features = features;
    }

    public double getLabel() {
        return _label;
    }

    public double[] getFeatures() {
        return _features;
    }
}
