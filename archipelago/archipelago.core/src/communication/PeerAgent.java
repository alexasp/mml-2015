package communication;

import communication.messaging.MessageFacade;
import jade.core.Agent;
import learning.EnsembleModel;
import learning.LabeledSample;
import learning.Model;
import privacy.NoisyQueryable;

/**
 * Created by alex on 3/5/15.
 */
public class PeerAgent extends Agent {

    private final MessageFacade _messageFacade;
    private EnsembleModel _ensemble;
    private NoisyQueryable<LabeledSample> _data;

    public PeerAgent(NoisyQueryable<LabeledSample> data, BehaviorFactory behaviorFactory, EnsembleModel ensemble, MessageFacade messageFacade) {
        _ensemble = ensemble;
        _data = data;
        _messageFacade = messageFacade;
        addBehaviour(behaviorFactory.getModelCreation(this));
        addBehaviour(behaviorFactory.getPeerUpdate(this, messageFacade));
    }

    public void addModel(Model model) {
        _ensemble.add(model);
    }

    //todo: consider making this private or require a new agent, to avoid breaching differential privacy
    public NoisyQueryable<LabeledSample> getData() {
        return _data;
    }

    public double getUpdateCost() {
        throw new UnsupportedOperationException();
    }

    public MessageFacade getMessageFacade() {
        return _messageFacade;
    }

    public void run(int iterations) {
    }
}
