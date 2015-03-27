package communication.peer.behaviours;

import communication.PeerAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import learning.Model;
import learning.ModelFactory;

/**
 * Created by alex on 3/17/15.
 */
public class ModelCreationBehavior extends OneShotBehaviour {
    private final PeerAgent _agent;
    private final ModelFactory _modelFactory;
    private int _parameters;

    public ModelCreationBehavior(PeerAgent agent, ModelFactory modelFactory, int parameters) {
        _agent = agent;
        _modelFactory = modelFactory;
        _parameters = parameters;
    }

    @Override
    public void action() {
        Model model = _modelFactory.getLogisticModel(_parameters); //TODO: add parameters
        model.update(_agent.getUpdateCost(), _agent.getData());
        _agent.addModel(model);
    }
}
