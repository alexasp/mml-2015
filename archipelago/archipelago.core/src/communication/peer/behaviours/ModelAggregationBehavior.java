package communication.peer.behaviours;

import communication.BehaviourFactory;
import communication.PeerAgent;
import communication.messaging.GroupMessage;
import communication.messaging.MessageFacade;
import communication.peer.ArchipelagoPerformatives;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;

import java.util.ArrayList;

/**
 * Created by alex on 4/22/15.
 */
public class ModelAggregationBehavior extends CyclicBehaviour{

    private PeerAgent _peerAgent;
    private MessageFacade _messageFacade;
    private BehaviourFactory _behaviorFactory;

    public ModelAggregationBehavior(PeerAgent peerAgent, MessageFacade messaging, BehaviourFactory behaviorFactory) {
        _peerAgent = peerAgent;
        _messageFacade = messaging;
        _behaviorFactory = behaviorFactory;
    }

    @Override
    public void action() {

        if(_messageFacade.hasMessage(ArchipelagoPerformatives.GroupFormation)){
            GroupMessage message = _messageFacade.nextAggregationGroupMessage();
            if(message.agents.indexOf(_peerAgent.getAID()) > 0) {
                _peerAgent.addBehaviour(_behaviorFactory.getContributorBehavior(_peerAgent, message.agents.get(0), _messageFacade, message.conversationId));
            }
            else {
                ArrayList<AID> parties = new ArrayList<>(message.agents);
                parties.remove(0);
                _peerAgent.addBehaviour(_behaviorFactory.getCuratorBehavior(parties, _messageFacade, _peerAgent, message.conversationId));
            }
        }
        else{
            block();
        }
    }
}
