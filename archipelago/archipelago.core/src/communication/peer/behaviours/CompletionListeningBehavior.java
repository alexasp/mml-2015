package communication.peer.behaviours;

import communication.messaging.MessageFacade;
import communication.peer.ArchipelagoPerformatives;
import communication.peer.CompletionListeningAgent;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Created by aspis on 26.03.2015.
 */
public class CompletionListeningBehavior extends CyclicBehaviour {
    private CompletionListeningAgent _agent;
    private MessageFacade _messageFacade;
    public static final ArchipelagoPerformatives Performative = ArchipelagoPerformatives.IterationsCompleted;

    public CompletionListeningBehavior(CompletionListeningAgent agent, MessageFacade messageFacade) {
        _agent = agent;
        _messageFacade = messageFacade;
    }

    @Override
    public void action() {
        if(_messageFacade.hasMessage(Performative)){
            _messageFacade.nextMessage(Performative);
            _agent.anAgentCompleted();
        }
        else{
            block();
        }
    }
}
