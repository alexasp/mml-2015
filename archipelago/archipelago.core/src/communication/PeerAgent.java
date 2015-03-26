package communication;

import communication.messaging.MessageFacade;
import communication.messaging.MessageFacadeFactory;
import jade.core.Agent;
import learning.EnsembleModel;
import learning.LabeledSample;
import learning.Model;
import privacy.NoisyQueryable;

import java.util.List;

/**
 * Created by alex on 3/5/15.
 */
public class PeerAgent extends Agent {

    private final MessageFacade _messageFacade;
    private EnsembleModel _ensemble;
    private NoisyQueryable<LabeledSample> _data;
    private int _iterations;

    public PeerAgent(NoisyQueryable<LabeledSample> data, BehaviourFactory behaviourFactory, EnsembleModel ensemble, MessageFacadeFactory messageFacadeFactory, int iterations) {
        _ensemble = ensemble;
        _data = data;
        _iterations = iterations;
        _messageFacade = messageFacadeFactory.getFacade(this);
        addBehaviour(behaviourFactory.getModelCreation(this));
        addBehaviour(behaviourFactory.getPeerUpdate(this, _messageFacade));
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
        throw new UnsupportedOperationException();
    }

    public List<Double> labelData(NoisyQueryable<LabeledSample> test) {
        throw new UnsupportedOperationException();
    }

    public int getIterations() {
        return _iterations;
    }
}
