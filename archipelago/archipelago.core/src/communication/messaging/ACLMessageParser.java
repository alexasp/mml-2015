package communication.messaging;

import com.google.inject.Inject;
import communication.messaging.jade.ACLMessageReader;
import communication.peer.ArchipelagoPerformatives;
import communication.peer.behaviours.CompletionListeningBehavior;
import communication.peer.behaviours.PeerUpdateBehavior;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import learning.ParametricModel;
import learning.ModelFactory;

import java.util.List;

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

    public ACLMessage createModelMessage(ParametricModel model, AID agent, ArchipelagoPerformatives performative) {
        ACLMessage message = new ACLMessage(PeerUpdateBehavior.Performative.ordinal());
        message.setContent(model.serialize());
        message.setOntology(Ontologies.Model.name());
        message.addReceiver(agent);

        return message;
    }

    public ACLMessage createCompletionMessage(AID agent1) {
        ACLMessage message = new ACLMessage(CompletionListeningBehavior.Performative.ordinal());
        message.setOntology(Ontologies.Message.name());
        message.setContent(agent1.toString());
        return message;
    }

    public ACLMessage createModelMessage(ParametricModel model, AID agent2) {
        return createModelMessage(model, agent2, null);
    }

    public ACLMessage createGroupMessage(List<AID> group) {
        ACLMessage message = new ACLMessage(ArchipelagoPerformatives.GroupFormation.ordinal());
        for (AID aid : group) {
            message.addReceiver(aid);
        }
        message.setContent(serializeGroup(group));
        return message;
    }

    private String serializeGroup(List<AID> group) {
        throw new UnsupportedClassVersionError();
    }

    public GroupMessage parseGroupMessage(ACLMessage message) {
        List<AID> agents = deserializeGroup(message.getContent());
        String conversationId = deserializeId(message.getContent());
        return new GroupMessage(agents, conversationId);
    }

    private String deserializeId(String content) {
        throw new UnsupportedOperationException();
    }

    private List<AID> deserializeGroup(String content) {
        throw new UnsupportedOperationException();
    }
}
