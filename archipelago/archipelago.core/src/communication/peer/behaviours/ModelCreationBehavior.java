package communication.peer.behaviours;

import communication.BehaviourFactory;
import communication.PeerAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import learning.Model;
import learning.ModelFactory;
import privacy.math.RandomGenerator;

import java.util.stream.IntStream;

/**
 * Created by alex on 3/17/15.
 */
public class ModelCreationBehavior extends OneShotBehaviour {
    private final PeerAgent _agent;
    private final ModelFactory _modelFactory;
    private RandomGenerator _randomGenerator;
    private BehaviourFactory _behaviorFactory;
    private int _parameters;

    public ModelCreationBehavior(PeerAgent agent, ModelFactory modelFactory, RandomGenerator randomGenerator, BehaviourFactory behaviorFactory, int parameters) {
        _agent = agent;
        _modelFactory = modelFactory;
        _randomGenerator = randomGenerator;
        _behaviorFactory = behaviorFactory;
        _parameters = parameters;
    }

    @Override
    public void action() {

        System.out.println(_agent.getName() + " created model.");

        double[] parameterVector = IntStream.range(0, _parameters)
                .mapToDouble(i -> _randomGenerator.uniform(-1.0, 1.0))
                .toArray();

        Model model = _modelFactory.getPrivateLogisticModel(parameterVector); //TODO: add parameters
        model.update(_agent.getUpdateCost(), _agent.getData());
        _agent.addModel(model);

//        _agent.addBehaviour(_behaviorFactory.getModelPropegate(_agent, model));

        _agent.getMessageFacade().sendToRandomPeer(model);
    }
}
