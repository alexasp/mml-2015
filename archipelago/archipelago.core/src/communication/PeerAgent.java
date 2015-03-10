package communication;

import communication.messaging.MessageFacade;
import jade.core.Agent;
import learning.EnsembleModel;
import learning.LabeledExample;
import learning.Model;
import privacy.NoisyQueryable;

/**
 * Created by alex on 3/5/15.
 */
public class PeerAgent extends Agent {

    private EnsembleModel _ensemble;
    private NoisyQueryable<LabeledExample> _data;

    public PeerAgent(NoisyQueryable<LabeledExample> data, BehaviorFactory behaviorFactory, EnsembleModel ensemble, MessageFacade messageFacade) {
        _ensemble = ensemble;
        _data = data;
        addBehaviour(behaviorFactory.getPeerUpdate(this, messageFacade));
    }

    public void addModel(Model model) {
        _ensemble.add(model);
    }

    //todo: consider making this private or require a new agent, to avoid breaching differential privacy
    public NoisyQueryable<LabeledExample> getData() {
        return _data;
    }
}
