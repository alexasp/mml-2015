package communication.messaging;

import communication.messaging.jade.ACLMessageReader;
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

}
