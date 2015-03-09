package communication;

import communication.messaging.MessageFacade;
import jade.core.Agent;
import learning.EnsembleModel;
import learning.Model;

/**
 * Created by alex on 3/5/15.
 */
public class PeerAgent extends Agent {

    private EnsembleModel _ensemble;

    public PeerAgent(BehaviorFactory behaviorFactory, EnsembleModel ensemble, MessageFacade messageFacade) {
        _ensemble = ensemble;

        addBehaviour(behaviorFactory.getPeerUpdate(this, messageFacade));
    }

    public void addModel(Model model) {
        _ensemble.add(model);
    }
}
