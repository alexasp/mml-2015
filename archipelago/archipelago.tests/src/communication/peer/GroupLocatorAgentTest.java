package communication.peer;

import communication.BehaviourFactory;
import communication.grouping.behaviors.GroupFormingBehaviour;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupLocatorAgentTest {


    private GroupLocatorAgent _agent;
    private BehaviourFactory _behaviorFactory;

    @Before
    public void setUp() {
        _behaviorFactory = mock(BehaviourFactory.class);
        when(_behaviorFactory.getGroupFormingBehaviour()).thenReturn(mock(GroupFormingBehaviour.class));
        _agent = new GroupLocatorAgent(_behaviorFactory);
    }

    @Test
    public void construction_GetsGroupingBehavior(){
        verify(_behaviorFactory).getGroupFormingBehaviour();
    }

}