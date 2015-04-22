package communication.peer.behaviours.aggregation;

import jade.core.behaviours.CyclicBehaviour;
import org.junit.Test;

import static org.junit.Assert.*;

public class CuratorBehaviorTest {

    @Test
    public void isCyclic() {
        assertTrue(new CuratorBehavior() instanceof CyclicBehaviour);
    }

    @Test
    public void action_AllParticipantsResponded_PublishesNoisyAveragedModel(){
        fail();
    }

}