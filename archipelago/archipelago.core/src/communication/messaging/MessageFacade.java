package communication.messaging;

import communication.peer.AggregationPerformative;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import learning.ParametricModel;
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

    public boolean hasMessage(int performative) {
        if(_nextMessage != null) { return true; }

        _nextMessage = _agent.receive(MessageTemplate.MatchPerformative(performative));
        return _nextMessage != null;
    }

    public Message nextMessage(int performative) {
        if(!hasMessage(performative)){ throw new IllegalStateException("No messages available, but nextMessage was called."); }

        if(_nextMessage == null){
            _nextMessage = _agent.receive();
        }

        Message parsedMessage = null;
        try {
            parsedMessage = _messageParser.parse(_nextMessage);
        } catch (OntologyException e) {
            throw new RuntimeException("Error when parsing message");
        }
        _nextMessage = null;

        return parsedMessage;
    }

    public void sendToRandomPeer(ParametricModel model) {
        List<AID> peers = _peerGraph.getPeers(_agent);

        if(peers.size() == 0)
            return;

        AID target = peers.get(_randomGenerator.uniform(0, peers.size() - 1));
        ACLMessage message = _messageParser.createModelMessage(model, target);

        _agent.send(message);
    }

    public void sendCompletionMessage(AID agent1) {
        ACLMessage message = _messageParser.createCompletionMessage(agent1);
        message.addReceiver(_peerGraph.getMonitoringAgent(_agent));

        _agent.send(message);
    }

    public void requestAggregationGroup() {
        throw new UnsupportedOperationException();
    }

    public List<AID> nextGroupMessage() {
        throw new UnsupportedOperationException();
    }

    public void sendToPeer(AID agentId, ParametricModel model, AggregationPerformative performative) {
        ACLMessage message = _messageParser.createModelMessage(model, agentId, performative);
        message.addReceiver(agentId);

        _agent.send(message);
    }
}
