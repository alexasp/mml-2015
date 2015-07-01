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

    private  Consumer<Experiment> _completionAction;
    private  int _totalPeerCount;
    private  MessageFacade _messageFacade;
    private  Behaviour _completionBehavior;
    private int _completedPeers;
    private Experiment _experiment;
    private int _iterations;
    private BehaviourFactory _behaviourFactory;

    public CompletionListeningAgent(MessageFacadeFactory messageFacadeFactory, BehaviourFactory behaviourFactory) {
        _behaviourFactory = behaviourFactory;
        _messageFacade = messageFacadeFactory.getFacade(this);
    }

    public void init(Consumer<Experiment> completionAction, int totalPeerCount, Experiment experiment, int iterations){

        _completionAction = completionAction;
        _totalPeerCount = totalPeerCount;
        _iterations = iterations;
        _experiment = experiment;
        _completedPeers = 0;

        _completionBehavior = _behaviourFactory.getCompletionListening(this, _messageFacade);
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
