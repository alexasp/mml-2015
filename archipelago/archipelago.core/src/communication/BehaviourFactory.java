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

    @Inject
    public BehaviourFactory(ModelFactory logisticModelFactory) {
        _modelFactory = logisticModelFactory;
    }

    public Behaviour getPeerUpdate(PeerAgent peerAgent, MessageFacade messageFacade) {
        return new PeerUpdateBehavior(peerAgent, messageFacade, this);
    }

    public Behaviour getModelPropegate(PeerAgent agent, Model model) {
        return new PropegateBehavior(model, agent);
    }

    public Behaviour getModelCreation(PeerAgent agent, int parameters) {
        return new ModelCreationBehavior(agent, _modelFactory, this, parameters);
    }

    public Behaviour getCompletionListening(CompletionListeningAgent agent, MessageFacade messageFacade) {
        return new CompletionListeningBehavior(agent, messageFacade);
    }

    public Behaviour getCompletionBehavior(PeerAgent peerAgent, MessageFacade facade) {
        return new CompletionBehaviour(peerAgent, facade);
    }

    public ModelAggregationBehavior getModelAggregation() {
        return new ModelAggregationBehavior();
    }
}
