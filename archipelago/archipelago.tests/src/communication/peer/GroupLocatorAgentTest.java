package communication.peer;

import communication.BehaviourFactory;
import communication.grouping.behaviors.GroupFormingBehaviour;
import communication.messaging.MessageFacade;
import communication.messaging.MessageFacadeFactory;
import experiment.ExperimentConfiguration;
import jade.core.AID;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupLocatorAgentTest {


    private GroupLocatorAgent _agent;
    private BehaviourFactory _behaviorFactory;
    private ExperimentConfiguration _configuration;
    private List<AID> _agents;
    private MessageFacadeFactory _messageFacadeFactory;
    private MessageFacade _messageFacade;

    @Before
    public void setUp() {
        _messageFacade = mock(MessageFacade.class);
        _messageFacadeFactory = mock(MessageFacadeFactory.class);
        when(_messageFacadeFactory.getFacade(any(GroupLocatorAgent.class))).thenReturn(_messageFacade);
        _configuration = mock(ExperimentConfiguration.class);
        _behaviorFactory = mock(BehaviourFactory.class);
        _agents = mock(List.class);
        when(_behaviorFactory.getGroupFormingBehaviour(any(GroupLocatorAgent.class), same(_agents), same(_configuration), same(_messageFacade)))
                .thenReturn(mock(GroupFormingBehaviour.class));

        _agent = new GroupLocatorAgent(_agents, _behaviorFactory, _configuration, _messageFacadeFactory);
    }

    @Test
    public void construction_GetsGroupingBehavior(){
        verify(_behaviorFactory).getGroupFormingBehaviour(_agent, _agents, _configuration, _messageFacade);
    }


}