package communication.peer.behaviours;

import communication.BehaviourFactory;
import communication.PeerAgent;
import experiment.ExperimentConfiguration;
import jade.core.behaviours.OneShotBehaviour;
import learning.ParametricModel;
import learning.ModelFactory;

import java.util.stream.IntStream;

/**
 * Created by alex on 3/17/15.
 */
public class ModelCreationBehavior extends OneShotBehaviour {
    private final PeerAgent _agent;
    private final ModelFactory _modelFactory;
    private BehaviourFactory _behaviorFactory;
    private int _parameters;
    private ExperimentConfiguration _configuration;

    public ModelCreationBehavior(PeerAgent agent, ModelFactory modelFactory, BehaviourFactory behaviorFactory, int parameters, ExperimentConfiguration configuration) {
        _agent = agent;
        _modelFactory = modelFactory;
        _behaviorFactory = behaviorFactory;
        _parameters = parameters;
        _configuration = configuration;
    }

    @Override
    public void action() {

//        System.out.println(_agent.getName() + " created model.");

        double[] parameterVector = IntStream.range(0, _parameters)
                .mapToDouble(i -> 0)
                .toArray();


        ParametricModel model = _modelFactory.getModel(parameterVector); //TODO: add parameters
        model.update(_agent.getUpdateCost(), _agent.getData());
        _agent.setLocalModel(model);

        if(_configuration.localModelInEnsemble)
        _agent.addModel(model);
    }
}
