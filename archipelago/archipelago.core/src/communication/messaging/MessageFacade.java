package communication.messaging;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import learning.Model;

/**
 * Created by alex on 3/9/15.
 */
public class MessageFacade {
    private final Agent _agent;
    private MessageParser _messageParser;
    private ACLMessage _nextMessage;


    public MessageFacade(Agent agent, MessageParser messageParser) {
        _agent = agent;
        _messageParser = messageParser;
    }

    public boolean hasMessage() {
        if(_nextMessage != null) { return true; }

        _nextMessage = _agent.receive();
        return _nextMessage != null;
    }

    public Message nextMessage() {
        if(!hasMessage()){ throw new IllegalStateException("No messages available, but nextMessage was called."); }

        if(_nextMessage == null){
            _nextMessage = _agent.receive();
        }

        Message parsedMessage = _messageParser.parse(_nextMessage);
        _nextMessage = null;

        return parsedMessage;
    }

    public void sendToRandomPeer(Model model) {
        throw new UnsupportedOperationException();
    }
}
