package communication.messaging;

import communication.messaging.jade.ACLMessageReader;
import jade.lang.acl.ACLMessage;
import learning.Model;
import learning.ModelFactory;
import learning.models.LogisticModel;
import org.junit.Before;
import org.junit.Test;
import sun.rmi.runtime.Log;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/23/15.
 */
public class MessageParserTest {

    private MessageParser _parser;

    private String _modelString = "2.0,1.5,-2.5,-100.0";
    private ACLMessageReader _reader;
    private ACLMessage _aclMessage;
    private ModelFactory _modelFactory;

    private LogisticModel _model;

    @Before
    public void setUp(){
        _reader = mock(ACLMessageReader.class);
        _aclMessage = mock(ACLMessage.class);
        when(_reader.read(_aclMessage)).thenReturn(_modelString);
        _modelFactory = mock(ModelFactory.class);
        _model = mock(LogisticModel.class);
        when(_modelFactory.getLogisticModel(2.0, 1.5, -2.5, -100.0)).thenReturn(_model);

        _parser = new MessageParser(_reader, _modelFactory);
    }

    @Test
    public void parse_MessageWithModel(){
        Message parsedMessage = _parser.parse(_aclMessage);

        assertEquals(_model, parsedMessage.getModel());
    }

}
