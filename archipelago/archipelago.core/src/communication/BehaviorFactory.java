package communication;

import communication.messaging.MessageFacade;
import communication.peer.behaviours.PeerUpdateBehavior;
import communication.peer.behaviours.PropegateBehavior;
import jade.core.behaviours.Behaviour;
import learning.Model;
import learning.ModelFactory;

/**
 * Created by alex on 3/9/15.
 */
public class BehaviorFactory {
    private ModelFactory _modelFactory;

    public BehaviorFactory(ModelFactory modelFactory) {
        _modelFactory = modelFactory;
    }

    public Behaviour getPeerUpdate(PeerAgent peerAgent, MessageFacade messageFacade) {
        return new PeerUpdateBehavior(peerAgent, messageFacade, this);
    }

    public Behaviour getModelPropegate(PeerAgent agent, Model model) {
        return new PropegateBehavior(model, agent);
    }

    public Behaviour getModelCreation(PeerAgent agent) {
        return new ModelCreationBehavior(agent, _modelFactory);
    }
}
