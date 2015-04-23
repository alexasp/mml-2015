package communication.peer.behaviours.aggregation;

import communication.PeerAgent;
import communication.messaging.Message;
import communication.messaging.MessageFacade;
import communication.peer.AggregationPerformative;
import experiment.ExperimentConfiguration;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import learning.ModelMerger;
import learning.ParametricModel;
import privacy.math.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 4/22/15.
 */
public class CuratorBehavior extends CyclicBehaviour{
    private final List<AID> _parties;
    private PeerAgent _peerAgent;
    private final MessageFacade _messageFacade;
    private ModelMerger _modelMerger;
    private RandomGenerator _randomGenerator;
    private ExperimentConfiguration _configuration;
    private List<ParametricModel> _models;
    private double _smallestSet = Double.MAX_VALUE;

    public CuratorBehavior(List<AID> parties, PeerAgent peerAgent, MessageFacade messageFacade, ModelMerger modelMerger, RandomGenerator randomGenerator, ExperimentConfiguration configuration) {
        _parties = parties;
        _peerAgent = peerAgent;
        _messageFacade = messageFacade;
        _modelMerger = modelMerger;
        _randomGenerator = randomGenerator;
        _configuration = configuration;

        _models = new ArrayList<>();
    }

    @Override
    public void action() {

        if(_messageFacade.hasMessage(AggregationPerformative.ModelContribution.ordinal())) {
            Message message = _messageFacade.nextMessage(AggregationPerformative.ModelContribution.ordinal());
            _models.add(message.getModel());
            int dataSetSize = Integer.parseInt(message.getContent());
            if(dataSetSize < _smallestSet){ _smallestSet = dataSetSize; }

            if(_models.size() >= _parties.size()){
                ParametricModel mergedModel = _modelMerger.merge(_models);
                double beta = 2.0/(_smallestSet*_peerAgent.getEpsilon()*_configuration.regularization);
                mergedModel.addTerm(_randomGenerator.fromLaplacian(beta, message.getModel().getParameters().length));
                publishModel(mergedModel);
                _peerAgent.removeBehaviour(this);
            }
        }
        else{
            block();
        }

    }

    private void publishModel(ParametricModel mergedModel) {
        for (AID agentId : _parties) {
            _messageFacade.sendToPeer(agentId, mergedModel, AggregationPerformative.AggregatedResult);
        }
    }
}
