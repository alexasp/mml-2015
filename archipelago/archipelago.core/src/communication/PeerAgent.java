package communication;

import jade.core.Agent;
import learning.Model;

/**
 * Created by alex on 3/5/15.
 */
public class PeerAgent extends Agent {
    public PeerAgent(BehaviorFactory behaviorFactory) {

        addBehaviour(behaviorFactory.getPeerUpdate());

    }

    public void addModel(Model model) {
        throw new UnsupportedOperationException();
    }
}
