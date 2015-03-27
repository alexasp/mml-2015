package experiment;

import com.google.inject.Inject;
import communication.Environment;
import communication.peer.AgentFactory;
import jade.wrapper.StaleProxyException;
import learning.LabeledSample;
import learning.metrics.PerformanceMetrics;

import java.util.List;

/**
 * Created by aspis on 25.03.2015.
 */
public class ExperimentFactory {
    private AgentFactory _peerFactory;
    private PerformanceMetrics _performanceMetrics;
    private Environment _environment;
    private DataLoader _dataLoader;

    @Inject
    public ExperimentFactory(AgentFactory peerFactory, PerformanceMetrics performanceMetrics, Environment environment, DataLoader dataLoader) {
        _peerFactory = peerFactory;
        _performanceMetrics = performanceMetrics;
        _environment = environment;
        _dataLoader = dataLoader;
    }

    public Experiment getExperiment(List<LabeledSample> samples, double trainRatio, int peerCount, double testCost, int iterations, double budget, int parameters) throws StaleProxyException {
        return new Experiment(samples, trainRatio, peerCount, _peerFactory, _performanceMetrics, testCost, _environment, iterations, _dataLoader, budget, parameters);
    }
}
