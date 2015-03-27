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
    private final double _budget;
    private double _testCost;
    private Environment _environment;
    private double _trainRatio;
    private int _peerCount;
    private int _iterations;

    public Experiment(List<LabeledSample> samples, double trainRatio, int peerCount, AgentFactory agentFactory, PerformanceMetrics performanceMetrics, double testCost, Environment environment, int iterations, DataLoader _dataLoader, double budget, int _parameters) throws StaleProxyException {
        _testCost = testCost;
        _trainRatio = trainRatio;
        _environment = environment;
        _peerCount = peerCount;
        _iterations = iterations;

        _agentFactory = agentFactory;
        _budget = budget;

        List<List<LabeledSample>> trainPartitioning = _dataLoader.partition(trainRatio, samples);
        _data = samples;
        _trainData = trainPartitioning.get(0);
        _testData = trainPartitioning.get(1);

        _performanceMetrics = performanceMetrics;
        _peers = agentFactory.createPeers(_trainData, peerCount, iterations, _budget, _parameters);


        registerPeers(_peers);
    }

    private void registerPeers(List<PeerAgent> peers) throws StaleProxyException {
        for(PeerAgent peer : peers){
            _environment.registerAgent(peer);
        }
    }

    public void run(Consumer<Experiment> completionAction) throws ControllerException {
        _environment.registerAgent(_agentFactory.getCompletionAgent(completionAction, _peerCount, this));
        _environment.run();
    }

    public List<Double> test() {
        return _peers.stream()
                .mapToDouble(peer -> _performanceMetrics.errorRate(_testData, peer.labelData(_testData), _testCost))
                .boxed()
                .collect(Collectors.toList());
    }

    public double getTrainRatio() {
        return _trainRatio;
    }

    public List<LabeledSample> getData() {
        return _data;
    }

    public int getPeerCount() {
        return _peerCount;
    }

    public double getTestCost() {
        return _testCost;
    }

    public int getIterations() {
        return _iterations;
    }

}
