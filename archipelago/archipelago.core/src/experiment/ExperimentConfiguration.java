package experiment;

/**
 * Created by Alexander on 3/30/2015.
 */
public class ExperimentConfiguration {

    public int recordsPerPeer;
    public int aggregations;
    public double budget;
    public double trainRatio;
    public int peerCount;
    public double testCost;
    public int parameters;
    public double epsilon;
    public double regularization;
    public int groupSize;

    public ExperimentConfiguration(int aggregations, double budget, double trainRatio, int peerCount, double testCost, int parameters, double epsilon, double regularization, int groupSize, int recordsPerPeer) {
        this.aggregations = aggregations;
        this.budget = budget;
        this.trainRatio = trainRatio;
        this.peerCount = peerCount;
        this.testCost = testCost;
        this.parameters = parameters;
        this.epsilon = epsilon;
        this.regularization = regularization;
        this.groupSize = groupSize;
        this.recordsPerPeer = recordsPerPeer;
    }

    public ExperimentConfiguration() {

    }
}
