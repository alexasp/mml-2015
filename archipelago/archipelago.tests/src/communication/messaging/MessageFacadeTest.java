package communication.messaging;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/23/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Agent.class)
public class MessageFacadeTest {


    private MessageFacade _messaging;
    private Agent _agent;
    private ACLMessageParser _messageParser;

    @Before
    public void setUp(){
        _messageParser = mock(ACLMessageParser.class);
        _agent = PowerMockito.mock(Agent.class);
        _messaging = new MessageFacade(_agent, _messageParser);
    }


    @Test
    public void hasMessage_MessageAvailable_ChecksJadeAgentMessages(){
        ACLMessage message = mock(ACLMessage.class);
        when(_agent.receive()).thenReturn(message);

        boolean hasMessage = _messaging.hasMessage();

        assertTrue(hasMessage);
    }

    @Test
    public void hasMessage_MessageAvailable_DoesNotConsumeMessage(){
        ACLMessage message = mock(ACLMessage.class);
        when(_agent.receive()).thenReturn(message);
        Message parsedMessage = mock(Message.class);
        when(_messageParser.parse(message)).thenReturn(parsedMessage);

        boolean hasMessage = _messaging.hasMessage();
        Message actualMessage = _messaging.nextMessage();

        assertEquals(parsedMessage, actualMessage);
    }

    @Test
    public void nextMessage_ACLMessageAvailable_GetsParsedMessage(){
        ACLMessage message = mock(ACLMessage.class);
        when(_agent.receive()).thenReturn(message);
        Message parsedMessage = mock(Message.class);
        when(_messageParser.parse(message)).thenReturn(parsedMessage);

        Message actualMessage = _messaging.nextMessage();

        assertEquals(parsedMessage, actualMessage);
    }

    @Test(expected = IllegalStateException.class)
    public void nextMessage_NoMessageAvailable_Throws(){
        when(_agent.receive()).thenReturn(null);

        _messaging.nextMessage();
    }

}