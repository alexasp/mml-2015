package communication.peer.behaviours;

import communication.BehaviorFactory;
import communication.PeerAgent;
import communication.messaging.Message;
import communication.messaging.MessageFacade;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Created by alex on 3/9/15.
 */
public class PeerUpdateBehavior extends CyclicBehaviour {

    private final PeerAgent _peerAgent;
    private final MessageFacade _messageFacade;
    private final BehaviorFactory _behaviorFactory;

    public PeerUpdateBehavior(PeerAgent peerAgent, MessageFacade messageFacade, BehaviorFactory behaviorFactory) {
        _peerAgent = peerAgent;
        _messageFacade = messageFacade;
        _behaviorFactory = behaviorFactory;
    }

    @Override
    public void action() {
        if(_messageFacade.hasMessage()){
            Message message = _messageFacade.nextMessage();
            _peerAgent.addModel(message.getModel());

            _peerAgent.addBehaviour(_behaviorFactory.getModelPropegate(message.getModel()));
        }
    }

}
