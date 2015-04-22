package communication.peer.behaviours;

import communication.BehaviourFactory;
import communication.PeerAgent;
import communication.messaging.Message;
import communication.messaging.MessageFacade;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by alex on 3/9/15.
 */
public class PeerUpdateBehavior extends CyclicBehaviour {


    private final PeerAgent _peerAgent;
    private final MessageFacade _messageFacade;
    private final BehaviourFactory _behaviourFactory;
    private int _iteration;
    public static final int Performative = ACLMessage.PROPAGATE;

    public PeerUpdateBehavior(PeerAgent peerAgent, MessageFacade messageFacade, BehaviourFactory behaviourFactory) {
        _peerAgent = peerAgent;
        _messageFacade = messageFacade;
        _behaviourFactory = behaviourFactory;
        _iteration = 0;
    }

    @Override
    public void action() {
        if(_iteration <= _peerAgent.getIterations() - 1) {
            _peerAgent.addBehaviour(_behaviourFactory.getModelAggregation(_peerAgent, _messageFacade));
        }
        if (_iteration == _peerAgent.getIterations() - 1){
            _peerAgent.addBehaviour(_behaviourFactory.getCompletionBehavior(_peerAgent, _messageFacade));
        }

        _iteration++;

        if(_messageFacade.hasMessage(Performative)){

            Message message = _messageFacade.nextMessage(Performative);
            _peerAgent.addModel(message.getModel());
        }
        else if(_iteration >= _peerAgent.getIterations()){
            block(); //this method call ensures that this behavior is marked as inactive until a new message arrives.
        }
    }

}
