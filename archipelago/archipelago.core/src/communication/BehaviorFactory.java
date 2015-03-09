package communication;

import communication.messaging.MessageFacade;
import communication.peer.behaviours.PeerUpdateBehavior;
import jade.core.behaviours.Behaviour;

/**
 * Created by alex on 3/9/15.
 */
public class BehaviorFactory {
    public Behaviour getPeerUpdate(PeerAgent peerAgent, MessageFacade messageFacade) {
        return new PeerUpdateBehavior(peerAgent, messageFacade);
    }
}
