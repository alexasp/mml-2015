package communication;

import com.google.inject.Inject;
import communication.messaging.MessageFacade;
import communication.peer.CompletionListeningAgent;
import communication.peer.behaviours.*;
import jade.core.behaviours.Behaviour;
import learning.Model;
import learning.ModelFactory;
import privacy.math.RandomGenerator;

/**
 * Created by alex on 3/9/15.
 */
public class BehaviourFactory {
    private ModelFactory _modelFactory;
    private RandomGenerator _randomGenerator;

    @Inject
    public BehaviourFactory(ModelFactory logisticModelFactory, RandomGenerator randomGenerator) {
        _modelFactory = logisticModelFactory;
        _randomGenerator = randomGenerator;
    }

    public Behaviour getPeerUpdate(PeerAgent peerAgent, MessageFacade messageFacade) {
        return new PeerUpdateBehavior(peerAgent, messageFacade, this);
    }

    public Behaviour getModelPropegate(PeerAgent agent, Model model) {
        return new PropegateBehavior(model, agent);
    }

    public Behaviour getModelCreation(PeerAgent agent, int parameters) {
        return new ModelCreationBehavior(agent, _modelFactory, _randomGenerator, this, parameters);
    }

    public Behaviour getCompletionListening(CompletionListeningAgent agent, MessageFacade messageFacade) {
        return new CompletionListeningBehavior(agent, messageFacade);
    }

    public Behaviour getCompletionBehavior(PeerAgent peerAgent, MessageFacade facade) {
        return new CompletionBehaviour(peerAgent, facade);
    }
}
