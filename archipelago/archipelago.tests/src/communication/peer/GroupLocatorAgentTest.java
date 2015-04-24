package communication.peer;

import communication.BehaviourFactory;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GroupLocatorAgentTest {


    private GroupLocatorAgent _agent;
    private BehaviourFactory _behaviorFactory;

    @Before
    public void setUp() {
        _behaviorFactory = mock(BehaviourFactory.class);
        _agent = new GroupLocatorAgent();
    }


    @Test
    public void construction_GetsGroupingBehavior(){
        verify(_behaviorFactory).getGroupFormingBehaviour();
    }

}