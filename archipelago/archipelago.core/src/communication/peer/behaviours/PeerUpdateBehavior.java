package communication.peer.behaviours;

import communication.BehaviourFactory;
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
    private final BehaviourFactory _behaviourFactory;
    private int _iteration;

    public PeerUpdateBehavior(PeerAgent peerAgent, MessageFacade messageFacade, BehaviourFactory behaviourFactory) {
        _peerAgent = peerAgent;
        _messageFacade = messageFacade;
        _behaviourFactory = behaviourFactory;
        _iteration = 0;
    }

    @Override
    public void action() {
        if(_messageFacade.hasMessage()){
            Message message = _messageFacade.nextMessage();
            _peerAgent.addModel(message.getModel());

            if(_iteration < _peerAgent.getIterations() - 1) {
                _peerAgent.addBehaviour(_behaviourFactory.getModelPropegate(_peerAgent, message.getModel()));
                _iteration++;
            }
        }
    }

}
