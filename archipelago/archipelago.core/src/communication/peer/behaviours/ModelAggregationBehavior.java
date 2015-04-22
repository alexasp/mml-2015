package communication.peer.behaviours;

import communication.PeerAgent;
import communication.messaging.MessageFacade;
import communication.peer.AggregationPerformative;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.introspection.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

/**
 * Created by alex on 4/22/15.
 */
public class ModelAggregationBehavior extends CyclicBehaviour{

    private PeerAgent _peerAgent;
    private MessageFacade _messaging;
    private boolean groupRequested;

    public ModelAggregationBehavior(PeerAgent peerAgent, MessageFacade messaging) {
        _peerAgent = peerAgent;
        _messaging = messaging;

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
            _messaging.sendToPeer(group.get(0), _peerAgent.getLocalModel(), AggregationPerformative.ModelContribution);
        }
    }
}
