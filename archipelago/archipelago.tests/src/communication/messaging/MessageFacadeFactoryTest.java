package communication.messaging;

import jade.core.Agent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Created by alex on 3/23/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Agent.class)
public class MessageFacadeFactoryTest {

    @Test
    public void getMessageFacade(){
        ACLMessageParser parser = mock(ACLMessageParser.class);
        MessageFacadeFactory factory = new MessageFacadeFactory(parser);
        Agent agent = mock(Agent.class);

        MessageFacade facade = factory.getFacade(agent);

        assertNotNull(facade);
    }

}
