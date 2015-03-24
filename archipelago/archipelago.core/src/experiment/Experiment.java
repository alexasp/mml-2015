package experiment;

import communication.PeerAgent;
import communication.peer.PeerFactory;
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

    public Experiment(NoisyQueryable<LabeledSample> samples, double trainRatio, int peerCount, PeerFactory peerFactory, PerformanceMetrics performanceMetrics, double testCost) {
        _testCost = testCost;

        List<NoisyQueryable<LabeledSample>> trainPartitioning = samples.partition(trainRatio);
        _trainData = trainPartitioning.get(0);
        _testData = trainPartitioning.get(1);

        _performanceMetrics = performanceMetrics;
        _peers = peerFactory.createPeers(_trainData, peerCount);
    }

    public void run(int iterations) {
        for(PeerAgent peer : _peers){
            peer.run(iterations);
        }
    }

    public List<Double> test() {
        return _peers.stream()
                .mapToDouble(peer -> _performanceMetrics.errorRate(_testData, peer.labelData(_testData), _testCost))
                .boxed()
                .collect(Collectors.toList());
    }
}
