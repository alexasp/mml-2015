package communication.peer.behaviours;

import communication.BehaviourFactory;
import communication.PeerAgent;
import communication.messaging.Message;
import communication.messaging.MessageFacade;
import communication.peer.ArchipelagoPerformatives;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Created by alex on 3/9/15.
 */
public class PeerUpdateBehavior extends CyclicBehaviour {


    private final PeerAgent _peerAgent;
    private final MessageFacade _messageFacade;
    public static final ArchipelagoPerformatives Performative = ArchipelagoPerformatives.ModelPropegation;

    public PeerUpdateBehavior(PeerAgent peerAgent, MessageFacade messageFacade) {
        _peerAgent = peerAgent;
        _messageFacade = messageFacade;
    }

    @Override
    public void action() {

        if(_messageFacade.hasMessage(Performative)){
            Message message = _messageFacade.nextMessage(Performative);
            _peerAgent.addModel(message.getModel());
        }
        else {
            block(); //this method call ensures that this behavior is marked as inactive until a new message arrives.
        }
    }

}
