package communication.peer.behaviours.aggregation;

import communication.PeerAgent;
import communication.messaging.MessageFacade;
import communication.peer.AggregationPerformative;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Created by alex on 4/22/15.
 */
public class ContributorBehavior extends CyclicBehaviour{
    private final PeerAgent _peerAgent;
    private final AID _curator;
    private boolean modelSent;
    private MessageFacade _messageFacade;

    public ContributorBehavior(PeerAgent peerAgent, AID curator, MessageFacade messageFacade) {
        _peerAgent = peerAgent;
        _curator = curator;
        _messageFacade = messageFacade;
        modelSent = false;
    }

    @Override
    public void action() {

        if(!modelSent){
            _messageFacade.sendToPeer(_curator, _peerAgent.getLocalModel(), AggregationPerformative.ModelContribution);
        }

    }
}
