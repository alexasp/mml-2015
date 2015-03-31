package communication.messaging;

import com.google.inject.Inject;
import communication.messaging.jade.ACLMessageReader;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import learning.Model;
import learning.ModelFactory;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by alex on 3/23/15.
 */
public class ACLMessageParser {
    private static final String DELIMITER = ",";
    private final ACLMessageReader _reader;
    private final ModelFactory _modelFactory;

    @Inject
    public ACLMessageParser(ACLMessageReader reader, ModelFactory modelFactory) {
        _reader = reader;
        _modelFactory = modelFactory;
    }

    public Message parse(ACLMessage message) {
        String content = _reader.read(message);
        String[] parts = content.split(DELIMITER);
        double[] parameters = IntStream.range(0, parts.length)
                .mapToDouble(i -> Double.parseDouble(parts[i]))
                .toArray();

        return new Message(_modelFactory.getLogisticModel(parameters));
    }

    public ACLMessage createMessage(Model model, AID agent2) {
        throw new UnsupportedOperationException();
    }

    public ACLMessage createCompletionMessage(AID agent1) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(agent1.toString());
        //todo get CompletionListeningAgent from DF service and add as recipient
//        message.addReceiver();
        return message;
    }
}
