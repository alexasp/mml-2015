package communication;

import communication.messaging.MessageFacade;
import communication.messaging.MessageFacadeFactory;
import communication.messaging.PeerGraph;
import jade.core.Agent;
import learning.EnsembleModel;
import learning.LabeledSample;
import learning.Model;
import privacy.NoisyQueryable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 3/5/15.
 */
public class PeerAgent extends Agent {

    public static final String SERVICE_NAME = "peer";
    private final MessageFacade _messageFacade;
    private EnsembleModel _ensemble;
    private NoisyQueryable<LabeledSample> _data;
    private int _iterations;
    private int _parameters;
    private double _updateCost;

    public PeerAgent(NoisyQueryable<LabeledSample> data, BehaviourFactory behaviourFactory, EnsembleModel ensemble, MessageFacadeFactory messageFacadeFactory, int iterations, PeerGraph _peerGraph, int parameters, double updateCost) {
        _ensemble = ensemble;
        _data = data;
        _iterations = iterations;
        _parameters = parameters;
        _updateCost = updateCost;
        _messageFacade = messageFacadeFactory.getFacade(this);

        addBehaviour(behaviourFactory.getModelCreation(this, parameters));
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
        return _updateCost;
    }

    public MessageFacade getMessageFacade() {
        return _messageFacade;
    }

    public List<Double> labelData(List<LabeledSample> test) {
        throw new UnsupportedOperationException();
    }

    public int getIterations() {
        return _iterations;
    }


    public int getParameterLength() {
        return _parameters;
    }

    public ArrayList<Model> getModels() {
        return _ensemble.getModels();
    }
}
