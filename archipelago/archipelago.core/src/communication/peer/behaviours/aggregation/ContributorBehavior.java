package communication.peer.behaviours.aggregation;

import communication.PeerAgent;
import communication.messaging.Message;
import communication.messaging.MessageFacade;
import communication.peer.ArchipelagoPerformatives;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Created by alex on 4/22/15.
 */
public class ContributorBehavior extends OneShotBehaviour{
    private final PeerAgent _peerAgent;
    private final AID _curator;
    private String _conversationId;
    private MessageFacade _messageFacade;

    public ContributorBehavior(PeerAgent peerAgent, AID curator, String conversationId, MessageFacade messageFacade) {
        _peerAgent = peerAgent;
        _curator = curator;
        _conversationId = conversationId;
        _messageFacade = messageFacade;
    }

    @Override
    public void action() {
        _messageFacade.sendToPeer(_curator, _peerAgent.getLocalModel(), ArchipelagoPerformatives.ModelContribution, _conversationId, _peerAgent.getData().size());
    }
}
