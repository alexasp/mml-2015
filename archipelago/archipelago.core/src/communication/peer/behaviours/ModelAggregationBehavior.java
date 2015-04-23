package communication.peer.behaviours;

import communication.BehaviourFactory;
import communication.PeerAgent;
import communication.messaging.MessageFacade;
import communication.peer.AggregationPerformative;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;

import java.util.List;

/**
 * Created by alex on 4/22/15.
 */
public class ModelAggregationBehavior extends OneShotBehaviour{

    private PeerAgent _peerAgent;
    private MessageFacade _messageFacade;
    private boolean groupRequested;
    private BehaviourFactory _behaviorFactory;

    public ModelAggregationBehavior(PeerAgent peerAgent, MessageFacade messaging, BehaviourFactory behaviorFactory) {
        _peerAgent = peerAgent;
        _messageFacade = messaging;
        _behaviorFactory = behaviorFactory;

        groupRequested = false;
    }

    @Override
    public void action() {
        if(!groupRequested) {
            _messageFacade.requestAggregationGroup();
            groupRequested = true;
            block();
        }

        if(_messageFacade.hasMessage(AggregationPerformative.GroupFormation.ordinal())){
            List<AID> group = _messageFacade.nextGroupMessage();
            if(group.indexOf(_peerAgent.getAID()) > 0) {
                _peerAgent.addBehaviour(_behaviorFactory.getContributorBehavior(_peerAgent, group.get(0), _messageFacade));
            }
            else {
                group.remove(0);
                _peerAgent.addBehaviour(_behaviorFactory.getCuratorBehavior(group, _messageFacade, _peerAgent));
            }
        }
    }
}
