package communication.peer;

import communication.BehaviorFactory;
import communication.PeerAgent;
import communication.messaging.MessageFacade;
import communication.messaging.MessageFacadeFactory;
import learning.EnsembleModel;
import learning.LabeledSample;
import privacy.NoisyQueryable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 3/10/15.
 */
public class PeerFactory {
    private final MessageFacadeFactory _messageFacadeFactory;
    private BehaviorFactory _behaviorFactory;

    public PeerFactory(BehaviorFactory behaviorFactory, MessageFacadeFactory messageFacadeFactory) {
        _behaviorFactory = behaviorFactory;
        _messageFacadeFactory = messageFacadeFactory;
    }

    public List<PeerAgent> createPeers(NoisyQueryable<LabeledSample> data, int parts) {
        List<NoisyQueryable<LabeledSample>> partitions = data.partition(parts);

        List<PeerAgent> agents = new ArrayList<>();

        for(NoisyQueryable<LabeledSample> partition : partitions){
            agents.add(new PeerAgent(partition, _behaviorFactory, new EnsembleModel(), _messageFacadeFactory));
        }

        return agents;
    }
}
