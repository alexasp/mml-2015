package communication.peer.behaviours;

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

    public PeerUpdateBehavior(PeerAgent peerAgent, MessageFacade messageFacade) {
        _peerAgent = peerAgent;
        _messageFacade = messageFacade;
    }

    @Override
    public void action() {
        if(_messageFacade.hasMessage()){
            Message message = _messageFacade.nextMessage();
            _peerAgent.addModel(message.getModel());
        }
    }

}
