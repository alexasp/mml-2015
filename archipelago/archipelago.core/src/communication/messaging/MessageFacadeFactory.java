package communication.messaging;

import com.google.inject.Inject;
import jade.core.Agent;
import privacy.math.RandomGenerator;

/**
 * Created by alex on 3/23/15.
 */
public class MessageFacadeFactory {


    private ACLMessageParser messageParser;
    private PeerGraph _peerGraph;
    private RandomGenerator _randomGenerator;

    @Inject
    public MessageFacadeFactory(ACLMessageParser messageParser, PeerGraph peerGraph, RandomGenerator randomGenerator) {
        this.messageParser = messageParser;
        _peerGraph = peerGraph;
        _randomGenerator = randomGenerator;
    }

    public MessageFacade getFacade(Agent agent) {
        return new MessageFacade(agent, messageParser, _peerGraph, _randomGenerator);
    }
}
