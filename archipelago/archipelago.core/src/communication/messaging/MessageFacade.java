package communication.messaging;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import learning.Model;

/**
 * Created by alex on 3/9/15.
 */
public class MessageFacade {
    private final Agent _agent;
    private ACLMessage _nextMessage;


    public MessageFacade(Agent agent) {
        _agent = agent;
    }

    public boolean hasMessage() {
        if(_nextMessage != null) { return true; }

        _nextMessage = _agent.receive();
        return _nextMessage != null;
    }

    public Message nextMessage() {
        throw new UnsupportedOperationException("not implemented");
    }

    public void sendToRandomPeer(Model model) {
        throw new UnsupportedOperationException();
    }
}
