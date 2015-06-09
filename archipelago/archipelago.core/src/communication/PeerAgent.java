package communication;

import communication.messaging.MessageFacade;
import communication.messaging.MessageFacadeFactory;
import communication.messaging.PeerGraph;
import jade.core.Agent;
import learning.EnsembleModel;
import learning.LabeledSample;
import learning.ParametricModel;

import java.util.List;

/**
 * Created by alex on 3/5/15.
 */
public class PeerAgent extends Agent {

    public static final String SERVICE_NAME = "peer";
    private final MessageFacade _messageFacade;
    private EnsembleModel _ensemble;
    private List _data;
    private int _iterations;
    private int _parameters;
    private double _updateCost;
    private ParametricModel _trainedModel;

    public PeerAgent(List data, BehaviourFactory behaviourFactory, EnsembleModel ensemble, MessageFacadeFactory messageFacadeFactory, int iterations, PeerGraph _peerGraph, int parameters, double updateCost) {
        _ensemble = ensemble;
        _data = data;
        _iterations = iterations;
        _parameters = parameters;
        _updateCost = updateCost;
        _messageFacade = messageFacadeFactory.getFacade(this);

        addBehaviour(behaviourFactory.getModelCreation(this, parameters));
        addBehaviour(behaviourFactory.getModelAggregation(this, _messageFacade));
        addBehaviour(behaviourFactory.getPeerUpdate(this, _messageFacade));
    }

    public void addModel(ParametricModel model) {
        if(!_ensemble.contains(model)) {
            _ensemble.add(model);
        }
    }

    //todo: consider making this private or require a new agent, to avoid breaching differential privacy
    public List getData() {
        return _data;
    }

    public double getUpdateCost() {
        return _updateCost;
    }

    public MessageFacade getMessageFacade() {
        return _messageFacade;
    }

    public List<Double> labelData(List<LabeledSample> test) {
        return _ensemble.label(test);
//        return getLocalModel().label(test);
    }

    public int getIterations() {
        return _iterations;
    }


    public int getParameterLength() {
        return _parameters;
    }

    public List<ParametricModel> getModels() {
        return _ensemble.getModels();
    }

    public ParametricModel getLocalModel() {
        return _trainedModel;
    }

    public void setLocalModel(ParametricModel model) {
        _trainedModel = model;
    }

    public List<Double> labelData(List<LabeledSample> testData, double threshold) {
        return _ensemble.label(testData, threshold);
    }
}
