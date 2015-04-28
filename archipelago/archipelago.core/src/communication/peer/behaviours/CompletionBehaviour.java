package communication.peer.behaviours;

import communication.PeerAgent;
import communication.messaging.MessageFacade;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Created by Alexander on 3/30/2015.
 */
public class CompletionBehaviour extends OneShotBehaviour{
    private String _conversationId;
    private final MessageFacade _messageFacade;

    public CompletionBehaviour(String conversationId, MessageFacade messageFacade) {
        _conversationId = conversationId;

        _messageFacade = messageFacade;
    }

    @Override
    public void action() {
        _messageFacade.sendCompletionMessage(_conversationId);
        System.out.println("FINISH!"  + _conversationId + " ");
    }
}
