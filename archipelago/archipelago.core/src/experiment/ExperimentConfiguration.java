package experiment;

/**
 * Created by Alexander on 3/30/2015.
 */
public class ExperimentConfiguration {

    public final int iterations;
    public final double budget;
    public final double trainRatio;
    public final int peerCount;
    public final double testCost;
    public final int parameters;
    public final double updateCost;

    public ExperimentConfiguration(int iterations, double budget, double trainRatio, int peerCount, double testCost, int parameters, double updateCost) {
        this.iterations = iterations;
        this.budget = budget;
        this.trainRatio = trainRatio;
        this.peerCount = peerCount;
        this.testCost = testCost;
        this.parameters = parameters;
        this.updateCost = updateCost;
    }
}
