package communication.grouping.behaviors;

import communication.messaging.MessageFacade;
import communication.peer.AggregationPerformative;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupFormingBehaviourTest {


    private GroupFormingBehaviour _behaviour;
    private MessageFacade _messageFacade;
    private AID _agent1;
    private AID _agent2;
    private AID _agent3;

    @Before
    public void setUp() {
        _agent1 = mock(AID.class);
        _agent2 = mock(AID.class);
        _agent3 = mock(AID.class);

        _messageFacade = mock(MessageFacade.class);
        when(_messageFacade.hasMessage(AggregationPerformative.AggregationGroupRequest.ordinal())).thenReturn(true);

        _behaviour = new GroupFormingBehaviour();
    }

    @Test
    public void isCyclic() {
        assertTrue(CyclicBehaviour.class.isAssignableFrom(GroupFormingBehaviour.class));
    }

    @Test
    public void action_NewMessage_AddsSenderToListOfPreparedAgents(){
        when(_messageFacade.nextGroupRequestMessage()).thenReturn(_agent1);

        _behaviour.action();
    }

}