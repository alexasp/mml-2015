package communication.peer.behaviours;

import communication.messaging.MessageFacade;
import communication.peer.CompletionListeningAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by aspis on 26.03.2015.
 */
public class CompletionListeningBehavior extends CyclicBehaviour {
    private CompletionListeningAgent _agent;
    private MessageFacade _messageFacade;
    public static final int Performative = ACLMessage.INFORM;
    public static final MessageTemplate CompletionPerformative = MessageTemplate.MatchPerformative(Performative);

    public CompletionListeningBehavior(CompletionListeningAgent agent, MessageFacade messageFacade) {
        _agent = agent;
        _messageFacade = messageFacade;
    }

    @Override
    public void action() {
        if(_messageFacade.hasMessage(CompletionPerformative)){
            _messageFacade.nextMessage(CompletionPerformative);
            _agent.anAgentCompleted();
        }
    }
}
