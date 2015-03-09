package communication.peer.behaviours;

import communication.PeerAgent;
import communication.messaging.Message;
import communication.messaging.MessageFacade;
import learning.Model;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by alex on 3/9/15.
 */
public class PeerUpdateBehaviorTest {

    private PeerUpdateBehavior _updateBehavior;
    private MessageFacade _messageFacade;
    private PeerAgent _peerAgent;

    @Before
    public void setUp(){
        _messageFacade = mock(MessageFacade.class);
        _peerAgent = mock(PeerAgent.class);

        _updateBehavior = new PeerUpdateBehavior(_peerAgent, _messageFacade);
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

    @Test
    public void action_NoNewMessage_DoesNotGetMessage(){
        when(_messageFacade.hasMessage()).thenReturn(false);

        _updateBehavior.action();

        verify(_peerAgent, never()).addModel(any(Model.class));
    }

}
