package communication;

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

    public ModelCreationBehavior(PeerAgent agent, ModelFactory modelFactory) {
        _agent = agent;
        _modelFactory = modelFactory;
    }

    @Override
    public void action() {
        Model model = _modelFactory.getLogisticModel();
        model.update(_agent.getUpdateCost(), _agent.getData());
        _agent.addModel(model);
    }
}
