package experiment;

import communication.Environment;
import communication.peer.PeerFactory;
import jade.wrapper.StaleProxyException;
import learning.LabeledSample;
import learning.metrics.PerformanceMetrics;
import privacy.NoisyQueryable;

/**
 * Created by aspis on 25.03.2015.
 */
public class ExperimentFactory {
    private PeerFactory _peerFactory;
    private PerformanceMetrics _performanceMetrics;
    private Environment _environment;

    public ExperimentFactory(PeerFactory peerFactory, PerformanceMetrics performanceMetrics, Environment environment) {
        _peerFactory = peerFactory;
        _performanceMetrics = performanceMetrics;
        _environment = environment;
    }

    public Experiment getExperiment(NoisyQueryable<LabeledSample> samples, double trainRatio, int peerCount, double testCost, int iterations) throws StaleProxyException {
        return new Experiment(samples, trainRatio, peerCount, _peerFactory, _performanceMetrics, testCost, _environment, iterations);
    }
}
