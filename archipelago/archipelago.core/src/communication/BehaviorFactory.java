package communication;

import communication.messaging.MessageFacade;
import communication.peer.behaviours.PeerUpdateBehavior;
import communication.peer.behaviours.PropegateBehavior;
import jade.core.behaviours.Behaviour;
import learning.Model;

/**
 * Created by alex on 3/9/15.
 */
public class BehaviorFactory {
    public Behaviour getPeerUpdate(PeerAgent peerAgent, MessageFacade messageFacade) {
        return new PeerUpdateBehavior(peerAgent, messageFacade, this);
    }

    public Behaviour getModelPropegate(PeerAgent agent, Model model) {
        return new PropegateBehavior(model, agent);
    }

    public Behaviour getModelCreation(PeerAgent agent) {
        return new ModelCreationBehavior(agent);
    }
}
