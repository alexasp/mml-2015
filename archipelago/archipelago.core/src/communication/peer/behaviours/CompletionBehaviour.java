package communication.peer.behaviours;

import communication.PeerAgent;
import communication.messaging.MessageFacade;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Created by Alexander on 3/30/2015.
 */
public class CompletionBehaviour extends OneShotBehaviour{
    private final PeerAgent _peerAgent;
    private final MessageFacade _messageFacade;

    public CompletionBehaviour(PeerAgent peerAgent, MessageFacade messageFacade) {
        _peerAgent = peerAgent;

        _messageFacade = messageFacade;
    }

    @Override
    public void action() {
        _messageFacade.sendCompletionMessage(_peerAgent.getAID());
        System.out.println("FINISH!"  + _peerAgent.getAID());
    }
}
