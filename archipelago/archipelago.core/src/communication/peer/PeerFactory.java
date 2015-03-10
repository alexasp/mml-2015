package communication.peer;

import communication.BehaviorFactory;
import communication.PeerAgent;
import communication.messaging.MessageFacade;
import learning.EnsembleModel;
import learning.LabeledExample;
import privacy.NoisyQueryable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 3/10/15.
 */
public class PeerFactory {
    private BehaviorFactory _behaviorFactory;

    public PeerFactory(BehaviorFactory behaviorFactory) {
        _behaviorFactory = behaviorFactory;
    }

    public List<PeerAgent> createPeers(NoisyQueryable<LabeledExample> data, int parts) {
        List<NoisyQueryable<LabeledExample>> partitions = data.partition(parts);

        List<PeerAgent> agents = new ArrayList<>();

        for(NoisyQueryable<LabeledExample> partition : partitions){
            agents.add(new PeerAgent(partition, _behaviorFactory, new EnsembleModel(), new MessageFacade()));
        }

        return agents;
    }
}
