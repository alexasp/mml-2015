package communication.messaging;

import communication.peer.AggregationPerformative;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import learning.Model;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import privacy.math.RandomGenerator;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
    private PeerGraph _peerGraph;
    private RandomGenerator _randomGenerator;
    private Model _model;

    @Before
    public void setUp() {
        _model = mock(Model.class);
        _peerGraph = mock(PeerGraph.class);
        _messageParser = mock(ACLMessageParser.class);
        _agent = PowerMockito.mock(Agent.class);
        _randomGenerator = mock(RandomGenerator.class);
        _messaging = new MessageFacade(_agent, _messageParser, _peerGraph, _randomGenerator);
    }


    @Test
    public void hasMessage_MessageAvailable_ChecksJadeAgentMessages(){
        ACLMessage message = mock(ACLMessage.class);
        when(_agent.receive(any(MessageTemplate.class))).thenReturn(message);

        boolean hasMessage = _messaging.hasMessage(0);

        assertTrue(hasMessage);
    }

    @Test
    public void hasMessage_MessageAvailable_DoesNotConsumeMessage() throws OntologyException {
        ACLMessage message = mock(ACLMessage.class);
        when(_agent.receive(any(MessageTemplate.class))).thenReturn(message);
        Message parsedMessage = mock(Message.class);
        when(_messageParser.parse(message)).thenReturn(parsedMessage);

        boolean hasMessage = _messaging.hasMessage(0);
        Message actualMessage = _messaging.nextMessage(0);

        assertEquals(parsedMessage, actualMessage);
    }

    @Test
    public void nextMessage_ACLMessageAvailable_GetsParsedMessage() throws OntologyException {
        ACLMessage message = mock(ACLMessage.class);
        when(_agent.receive(any(MessageTemplate.class))).thenReturn(message);
        Message parsedMessage = mock(Message.class);
        when(_messageParser.parse(message)).thenReturn(parsedMessage);

        Message actualMessage = _messaging.nextMessage(0);

        assertEquals(parsedMessage, actualMessage);
    }

    @Test
    public void sendToRandomPeer(){
        AID agent1 = mock(AID.class);
        AID agent2 = mock(AID.class);
        List<AID> agents = Arrays.asList(agent1, agent2);
        when(_peerGraph.getPeers(_agent)).thenReturn(agents);
        when(_randomGenerator.uniform(0, agents.size()-1)).thenReturn(1);
        ACLMessage message = mock(ACLMessage.class);
        when(_messageParser.createModelMessage(_model, agent2)).thenReturn(message);

        _messaging.sendToRandomPeer(_model);

        verify(_agent).send(message);
    }

    @Test(expected = IllegalStateException.class)
    public void nextMessage_NoMessageAvailable_Throws(){
        when(_agent.receive(any(MessageTemplate.class))).thenReturn(null);

        _messaging.nextMessage(0);
    }

    @Test
    public void sendCompletionMessage_GetsACLMessage(){
        AID agent1 = mock(AID.class);
        ACLMessage message = mock(ACLMessage.class);
        when(_messageParser.createCompletionMessage(agent1)).thenReturn(message);

        _messaging.sendCompletionMessage(agent1);

        verify(_agent).send(message);
    }

    @Test
    public void sendCompletionMessage_AddsRecipient() {
        AID completionAgent = mock(AID.class);
        when(_peerGraph.getMonitoringAgent(_agent)).thenReturn(completionAgent);
        AID agent1 = mock(AID.class);
        ACLMessage message = mock(ACLMessage.class);
        when(_messageParser.createCompletionMessage(agent1)).thenReturn(message);

        _messaging.sendCompletionMessage(agent1);

        verify(_agent).send(message);
        verify(message).addReceiver(completionAgent);
    }

    @Test
    public void sendToPeer(){
        AID agent = mock(AID.class);
        ACLMessage message = mock(ACLMessage.class);
        when(_messageParser.createModelMessage(same(_model), same(agent), any(AggregationPerformative.class))).thenReturn(message);

        verify(_agent).send(message);
    }

}
