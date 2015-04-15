package communication.peer.behaviours;

import communication.PeerAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import learning.Model;

/**
 * Created by alex on 3/16/15.
 */
public class PropegateBehavior extends OneShotBehaviour {

    private final Model _model;
    private final PeerAgent _peerAgent;

    public PropegateBehavior(Model model, PeerAgent peerAgent) {
        _model = model;
        _peerAgent = peerAgent;
    }


    @Override
    public void action() {
        _model.update(_peerAgent.getUpdateCost(), _peerAgent.getData());
        _peerAgent.getMessageFacade().sendToRandomPeer(_model);

//        System.out.println(_peerAgent.getName() + " propegated model.");
    }

}
