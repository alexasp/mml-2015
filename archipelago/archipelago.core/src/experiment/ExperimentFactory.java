package experiment;

import com.google.inject.Inject;
import communication.Environment;
import communication.messaging.PeerGraph;
import communication.peer.AgentFactory;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import learning.LabeledSample;
import learning.metrics.PerformanceMetrics;

import java.util.List;

/**
 * Created by aspis on 25.03.2015.
 */
public class ExperimentFactory {
    private AgentFactory _agentFactory;
    private PerformanceMetrics _performanceMetrics;
    private Environment _environment;
    private DataLoader _dataLoader;
    private PeerGraph _peerGraph;

    @Inject
    public ExperimentFactory(AgentFactory peerFactory, PerformanceMetrics performanceMetrics, Environment environment, DataLoader dataLoader, PeerGraph peerGraph) {
        _agentFactory = peerFactory;
        _performanceMetrics = performanceMetrics;
        _environment = environment;
        _dataLoader = dataLoader;
        _peerGraph = peerGraph;
    }


    public Experiment getExperiment(List<LabeledSample> samples, ExperimentConfiguration configuration) throws ControllerException {
        return new Experiment(samples, _agentFactory, _performanceMetrics, _environment, _dataLoader, _peerGraph, configuration);
    }
}
