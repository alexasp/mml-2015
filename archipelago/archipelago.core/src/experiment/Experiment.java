package experiment;

import communication.Environment;
import communication.PeerAgent;
import communication.peer.PeerFactory;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import learning.LabeledSample;
import learning.metrics.PerformanceMetrics;
import privacy.NoisyQueryable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alex on 3/17/15.
 */
public class Experiment {
    private final List<PeerAgent> _peers;
    private final PerformanceMetrics _performanceMetrics;
    private final NoisyQueryable<LabeledSample> _trainData;
    private final NoisyQueryable<LabeledSample> _testData;
    private double _testCost;
    private Environment _environment;

    public Experiment(NoisyQueryable<LabeledSample> samples, double trainRatio, int peerCount, PeerFactory peerFactory, PerformanceMetrics performanceMetrics, double testCost, Environment environment, int iterations) throws StaleProxyException {
        _testCost = testCost;
        _environment = environment;

        List<NoisyQueryable<LabeledSample>> trainPartitioning = samples.partition(trainRatio);
        _trainData = trainPartitioning.get(0);
        _testData = trainPartitioning.get(1);

        _performanceMetrics = performanceMetrics;
        _peers = peerFactory.createPeers(_trainData, peerCount, iterations);

        registerPeers(_peers);
    }

    private void registerPeers(List<PeerAgent> peers) throws StaleProxyException {
        for(PeerAgent peer : peers){
            _environment.registerAgent(peer);
        }
    }

    public void run() throws ControllerException {
        _environment.run();
    }

    public List<Double> test() {
        return _peers.stream()
                .mapToDouble(peer -> _performanceMetrics.errorRate(_testData, peer.labelData(_testData), _testCost))
                .boxed()
                .collect(Collectors.toList());
    }
}
