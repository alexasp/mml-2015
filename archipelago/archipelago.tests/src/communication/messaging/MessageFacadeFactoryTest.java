package communication.messaging;

import jade.core.Agent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
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
        MessageParser parser = mock(MessageParser.class);
        MessageFacadeFactory factory = new MessageFacadeFactory(parser);
        Agent agent = PowerMockito.mock(Agent.class);

        MessageFacade facade = factory.getFacade(agent);

        assertNotNull(facade);
    }

}
