package communication.messaging;

import com.google.inject.Inject;
import communication.PeerAgent;
import communication.messaging.jade.ACLMessageReader;
import communication.peer.CompletionListeningAgent;
import communication.peer.behaviours.CompletionListeningBehavior;
import communication.peer.behaviours.PeerUpdateBehavior;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import learning.Model;
import learning.ModelFactory;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static communication.peer.behaviours.CompletionListeningBehavior.CompletionPerformative;

/**
 * Created by alex on 3/23/15.
 */
public class ACLMessageParser {

    private final ACLMessageReader _reader;
    private final ModelFactory _modelFactory;

    @Inject
    public ACLMessageParser(ACLMessageReader reader, ModelFactory modelFactory) {
        _reader = reader;
        _modelFactory = modelFactory;
    }

    public Message parse(ACLMessage message) {
        String content = _reader.read(message);

        return new Message(_modelFactory.getPrivateLogisticModel(content));
    }

    public ACLMessage createMessage(Model model, AID agent2) {
        ACLMessage message = new ACLMessage(PeerUpdateBehavior.Performative);
        message.setContent(model.serialize());
        message.addReceiver(agent2);

        return message;
    }

    public ACLMessage createCompletionMessage(AID agent1) {
        ACLMessage message = new ACLMessage(CompletionListeningBehavior.Performative);
        message.setContent(agent1.toString());
        return message;
    }
}
