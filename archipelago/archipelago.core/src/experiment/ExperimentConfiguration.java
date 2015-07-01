package experiment;

import communication.peer.behaviours.aggregation.PublishTypes;

/**
 * Created by Alexander on 3/30/2015.
 */
public class ExperimentConfiguration {

    public int recordsPerPeer;
    public int aggregations;
    public double updateCost;
    public double trainRatio;
    public int peerCount;
    public double testCost;
    public int parameters;
    public double epsilon;
    public double regularization;
    public int groupSize;
    public int cvFolds;
    public boolean useCrossValidation;
    public PublishTypes publishType;
    public boolean localModelInEnsemble;
    public boolean classifyLocallyOnly;

    public ExperimentConfiguration(int aggregations, double budget, int peerCount, int parameters, double epsilon, double regularization, int groupSize, int recordsPerPeer, int cvFolds, boolean useCrossValidation) {
        this();
        this.useCrossValidation = useCrossValidation;
        this.cvFolds = cvFolds;
        this.aggregations = aggregations;
        this.updateCost = budget;
     //   this.trainRatio = trainRatio;
        this.peerCount = peerCount;
        this.parameters = parameters;
        this.epsilon = epsilon;
        this.regularization = regularization;
        this.groupSize = groupSize;
        this.recordsPerPeer = recordsPerPeer;
    }

    public ExperimentConfiguration() {
        this.localModelInEnsemble = true;
        this.classifyLocallyOnly = false;
    }
}
