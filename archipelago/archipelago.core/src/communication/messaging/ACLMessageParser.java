package communication.messaging;

import com.google.inject.Inject;
import communication.messaging.jade.ACLMessageReader;
import communication.peer.behaviours.CompletionListeningBehavior;
import communication.peer.behaviours.PeerUpdateBehavior;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import learning.Model;
import learning.LogisticModelFactory;
import learning.ModelFactory;

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

    public Message parse(ACLMessage message) throws OntologyException {
        String content = _reader.read(message);

        if(message.getOntology().equals(Ontologies.Message.name())) {
            return new Message(content);
        }
        else if(message.getOntology().equals(Ontologies.Model.name())){
            return new Message(_modelFactory.getModel(content));
        }
        else {
            throw new OntologyException("Ontology not recognized.");
        }
    }

    public ACLMessage createModelMessage(Model model, AID agent2) {
        ACLMessage message = new ACLMessage(PeerUpdateBehavior.Performative);
        message.setContent(model.serialize());
        message.setOntology(Ontologies.Model.name());
        message.addReceiver(agent2);

        return message;
    }

    public ACLMessage createCompletionMessage(AID agent1) {
        ACLMessage message = new ACLMessage(CompletionListeningBehavior.Performative);
        message.setOntology(Ontologies.Message.name());
        message.setContent(agent1.toString());
        return message;
    }
}
