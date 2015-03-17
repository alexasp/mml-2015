package experiment;

import communication.PeerAgent;
import communication.peer.PeerFactory;
import learning.LabeledSample;
import privacy.NoisyQueryable;

import java.util.List;

/**
 * Created by alex on 3/17/15.
 */
public class Experiment {
    private final List<PeerAgent> _peers;

    public Experiment(NoisyQueryable<LabeledSample> samples, double trainRatio, int peerCount, PeerFactory peerFactory) {
        List<NoisyQueryable<LabeledSample>> trainPartitioning = samples.partition(trainRatio);

        _peers = peerFactory.createPeers(trainPartitioning.get(0), peerCount);
    }

    public void run(int iterations) {
        for(PeerAgent peer : _peers){
            peer.run(iterations);
        }
    }
}
