package communication.messaging;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.lang.acl.ACLMessage;
import learning.Model;
import privacy.math.RandomGenerator;

import java.util.List;

/**
 * Created by alex on 3/9/15.
 */
public class MessageFacade {
    private final Agent _agent;
    private ACLMessageParser _messageParser;
    private PeerGraph _peerGraph;
    private RandomGenerator _randomGenerator;
    private ACLMessage _nextMessage;


    public MessageFacade(Agent agent, ACLMessageParser messageParser, PeerGraph peerGraph, RandomGenerator randomGenerator) {
        _agent = agent;
        _messageParser = messageParser;
        _peerGraph = peerGraph;
        _randomGenerator = randomGenerator;
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
        List<AID> peers = _peerGraph.getPeers(_agent);
        AID target = peers.get(_randomGenerator.uniform(0, peers.size() - 1));
        ACLMessage message = _messageParser.createMessage(model, target);
        
        _agent.send(message);
    }
}
