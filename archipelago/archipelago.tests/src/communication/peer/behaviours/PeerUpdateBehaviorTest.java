package communication.peer.behaviours;

import communication.PeerAgent;
import communication.messaging.Message;
import communication.messaging.MessageFacade;
import learning.Model;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/9/15.
 */
public class PeerUpdateBehaviorTest {

    private PeerUpdateBehavior _updateBehavior;
    private MessageFacade _messageFacade;
    private PeerAgent _peerAgent;

    @Before
    public void setUp(){
        _updateBehavior = new PeerUpdateBehavior();
    }

    @Test
    public void action_newMessage_AddsMessageToModelPool(){
        Message message = mock(Message.class);
        when(_messageFacade.hasMessage()).thenReturn(true);
        when(_messageFacade.nextMessage()).thenReturn(message);
        Model model = mock(Model.class);
        when(message.getModel()).thenReturn(model);

        _updateBehavior.action();

        verify(_peerAgent).addModel(model);
    }

}
