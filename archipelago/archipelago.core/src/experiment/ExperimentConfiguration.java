package experiment;

/**
 * Created by Alexander on 3/30/2015.
 */
public class ExperimentConfiguration {

    public int iterations;
    public double budget;
    public double trainRatio;
    public int peerCount;
    public double testCost;
    public int parameters;
    public double updateCost;
    public double regularization;
    public int groupSize;

    public ExperimentConfiguration(int iterations, double budget, double trainRatio, int peerCount, double testCost, int parameters, double updateCost, double regularization, int groupSize) {
        this.iterations = iterations;
        this.budget = budget;
        this.trainRatio = trainRatio;
        this.peerCount = peerCount;
        this.testCost = testCost;
        this.parameters = parameters;
        this.updateCost = updateCost;
        this.regularization = regularization;
        this.groupSize = groupSize;
    }
}
