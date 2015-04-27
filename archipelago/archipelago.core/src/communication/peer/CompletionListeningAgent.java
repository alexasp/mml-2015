package communication.peer;

import communication.BehaviourFactory;
import communication.messaging.MessageFacade;
import communication.messaging.MessageFacadeFactory;
import communication.messaging.PeerGraph;
import experiment.Experiment;
import jade.core.Agent;

import java.util.function.Consumer;

/**
 * Created by aspis on 25.03.2015.
 */
public class CompletionListeningAgent extends Agent {
    public static final String SERVICE_NAME = "COMPLETION";

    private final Consumer<Experiment> _completionAction;
    private final int _totalPeerCount;
    private final MessageFacade _messageFacade;
    private int _completedPeers = 0;
    private Experiment _experiment;

    public CompletionListeningAgent(Consumer<Experiment> completionAction, int totalPeerCount, BehaviourFactory behaviourFactory, MessageFacadeFactory messageFacadeFactory, Experiment experiment) {
        _completionAction = completionAction;
        _totalPeerCount = totalPeerCount;
        _messageFacade = messageFacadeFactory.getFacade(this);
        _experiment = experiment;

        addBehaviour(behaviourFactory.getCompletionListening(this, _messageFacade));
    }

    public void anAgentCompleted() {
        _completedPeers++;

//        if(_completedPeers == _totalPeerCount){
            _completionAction.accept(_experiment);
//        }
    }
}
