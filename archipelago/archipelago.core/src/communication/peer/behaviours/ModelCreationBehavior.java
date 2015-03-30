package communication.peer.behaviours;

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
    private int _parameters;

    public ModelCreationBehavior(PeerAgent agent, ModelFactory modelFactory, RandomGenerator randomGenerator, int parameters) {
        _agent = agent;
        _modelFactory = modelFactory;
        _randomGenerator = randomGenerator;
        _parameters = parameters;
    }

    @Override
    public void action() {
        double[] parameterVector = IntStream.range(0, _parameters)
                .mapToDouble(i -> _randomGenerator.uniform(-1.0, 1.0))
                .toArray();

        Model model = _modelFactory.getPrivateLogisticModel(parameterVector); //TODO: add parameters
        model.update(_agent.getUpdateCost(), _agent.getData());
        _agent.addModel(model);
    }
}
