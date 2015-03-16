package communication.peer.behaviours;

import jade.core.behaviours.SimpleBehaviour;
import learning.Model;

/**
 * Created by alex on 3/16/15.
 */
public class PropegateBehavior extends SimpleBehaviour {

    private final Model _model;

    public PropegateBehavior(Model model) {
        _model = model;
    }

    public Model getModel() {
        return null;
    }

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return false;
    }
}
