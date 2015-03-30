package experiment;

import communication.Environment;
import communication.PeerAgent;
import communication.peer.AgentFactory;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import learning.LabeledSample;
import learning.metrics.PerformanceMetrics;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by alex on 3/17/15.
 */
public class Experiment {
    private final List<PeerAgent> _peers;
    private final PerformanceMetrics _performanceMetrics;
    private final List<LabeledSample> _trainData;
    private final List<LabeledSample> _testData;
    private final List<LabeledSample> _data;
    private final AgentFactory _agentFactory;
    private final ExperimentConfiguration _configuration;

    private Environment _environment;


    public Experiment(List<LabeledSample> samples, AgentFactory agentFactory, PerformanceMetrics performanceMetrics, Environment environment, DataLoader dataLoader, ExperimentConfiguration configuration) throws StaleProxyException {
        _environment = environment;
        _agentFactory = agentFactory;

        _configuration = configuration;

        List<List<LabeledSample>> trainPartitioning = dataLoader.partition(configuration.trainRatio, samples);
        _data = samples;
        _trainData = trainPartitioning.get(0);
        _testData = trainPartitioning.get(1);

        _performanceMetrics = performanceMetrics;
        _peers = agentFactory.createPeers(_trainData, configuration.peerCount, configuration.iterations, configuration.budget, configuration.parameters);


        registerPeers(_peers);
    }

    private void registerPeers(List<PeerAgent> peers) throws StaleProxyException {
        for(PeerAgent peer : peers){
            _environment.registerAgent(peer);
        }
    }

    public void run(Consumer<Experiment> completionAction) throws ControllerException {
        _environment.registerAgent(_agentFactory.getCompletionAgent(completionAction, _configuration.peerCount, this));
        _environment.run();
    }

    public List<Double> test() {
        return _peers.stream()
                .mapToDouble(peer -> _performanceMetrics.errorRate(_testData, peer.labelData(_testData), _configuration.testCost))
                .boxed()
                .collect(Collectors.toList());
    }

    public double getTrainRatio() {
        return _configuration.trainRatio;
    }

    public List<LabeledSample> getData() {
        return _data;
    }

    public int getPeerCount() {
        return _configuration.peerCount;
    }

    public double getTestCost() {
        return _configuration.testCost;
    }

    public int getIterations() {
        return _configuration.iterations;
    }

}
