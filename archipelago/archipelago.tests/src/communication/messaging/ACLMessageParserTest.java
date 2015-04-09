package communication.messaging;

import communication.messaging.jade.ACLMessageReader;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import learning.LogisticModelFactory;
import org.junit.Before;
import org.junit.Test;
import privacy.learning.DifferentialLogisticModel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/23/15.
 */
public class ACLMessageParserTest {

    private ACLMessageParser _parser;

    private String _modelString = "2.0,1.5,-2.5,-100.0";
    private ACLMessageReader _reader;
    private ACLMessage _aclMessage;
    private LogisticModelFactory _Logistic_modelFactory;

    private DifferentialLogisticModel _model;

    @Before
    public void setUp(){
        _reader = mock(ACLMessageReader.class);
        _aclMessage = mock(ACLMessage.class);
        when(_reader.read(_aclMessage)).thenReturn(_modelString);
        _Logistic_modelFactory = mock(LogisticModelFactory.class);
        _model = mock(DifferentialLogisticModel.class);
        when(_Logistic_modelFactory.getModel(_modelString)).thenReturn(_model);

        _parser = new ACLMessageParser(_reader, _Logistic_modelFactory);
    }

    @Test
    public void parse_MessageWithModel() throws OntologyException {
        when(_aclMessage.getOntology()).thenReturn(Ontologies.Model.name());
        Message parsedMessage = _parser.parse(_aclMessage);

        assertEquals(_model, parsedMessage.getModel());
    }

    @Test
    public void parse_MessageOnly() throws OntologyException {
        String message = "hello";
        when(_reader.read(_aclMessage)).thenReturn(message);
        when(_aclMessage.getOntology()).thenReturn(Ontologies.Message.name());
        Message parsedMessage = _parser.parse(_aclMessage);

        assertEquals(message, parsedMessage.getContent());
    }

    @Test
    public void createMessage_SetsContent() {
        String content = "yo";
        AID aid = mock(AID.class);
        when(_model.serialize()).thenReturn(content);

        ACLMessage message = _parser.createModelMessage(_model, aid);

        assertEquals(content, message.getContent());
    }

    @Test
    public void createModelMessage_SetsModelOntology() {
        String content = "yo";
        AID aid = mock(AID.class);
        when(_model.serialize()).thenReturn(content);

        ACLMessage message = _parser.createModelMessage(_model, aid);

        assertEquals(Ontologies.Model.name(), message.getOntology());
    }

    @Test
    public void createCompletionMessage_SetsModelOntology() {
        AID aid = mock(AID.class);

        ACLMessage message = _parser.createCompletionMessage(aid);

        assertEquals(Ontologies.Message.name(), message.getOntology());
    }


}
