package communication.peer.behaviours;

import communication.BehaviourFactory;
import communication.PeerAgent;
import communication.messaging.MessageFacade;
import communication.peer.AggregationPerformative;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.introspection.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

/**
 * Created by alex on 4/22/15.
 */
public class ModelAggregationBehavior extends OneShotBehaviour{

    private PeerAgent _peerAgent;
    private MessageFacade _messaging;
    private boolean groupRequested;
    private BehaviourFactory _behaviorFactory;

    public ModelAggregationBehavior(PeerAgent peerAgent, MessageFacade messaging, BehaviourFactory behaviorFactory) {
        _peerAgent = peerAgent;
        _messaging = messaging;
        _behaviorFactory = behaviorFactory;

        groupRequested = false;
    }

    @Override
    public void action() {
        if(!groupRequested) {
            _messaging.requestAggregationGroup();
            groupRequested = true;
            block();
        }

        if(_messaging.hasMessage(AggregationPerformative.GroupFormation.ordinal())){
            List<AID> group = _messaging.nextGroupMessage();
            if(group.indexOf(_peerAgent.getAID()) > 0) {
                _peerAgent.addBehaviour(_behaviorFactory.getContributorBehavior(_peerAgent, group.get(0)));
            }
            else {
                _peerAgent.addBehaviour(_behaviorFactory.getCuratorBehavior());
            }
        }
    }
}
