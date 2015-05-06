package communication.peer;

import communication.BehaviourFactory;
import communication.messaging.MessageFacade;
import communication.messaging.MessageFacadeFactory;
import communication.messaging.PeerGraph;
import experiment.Experiment;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

import java.util.function.Consumer;

/**
 * Created by aspis on 25.03.2015.
 */
public class CompletionListeningAgent extends Agent {
    public static final String SERVICE_NAME = "COMPLETION";

    private final Consumer<Experiment> _completionAction;
    private final int _totalPeerCount;
    private final MessageFacade _messageFacade;
    private final Behaviour _completionBehavior;
    private int _completedPeers = 0;
    private Experiment _experiment;
    private int _iterations;

    public CompletionListeningAgent(Consumer<Experiment> completionAction, int totalPeerCount, BehaviourFactory behaviourFactory, MessageFacadeFactory messageFacadeFactory, Experiment experiment, int iterations) {
        _completionAction = completionAction;
        _totalPeerCount = totalPeerCount;
        _iterations = iterations;
        _messageFacade = messageFacadeFactory.getFacade(this);
        _experiment = experiment;

        _completionBehavior = behaviourFactory.getCompletionListening(this, _messageFacade);
        addBehaviour(_completionBehavior);
    }

    public void aModelCompleted() {
        _completedPeers++;

        if(_completedPeers == _iterations){
            removeBehaviour(_completionBehavior);

            while (receive() != null) { }

            _completionAction.accept(_experiment);
        }
    }


}
