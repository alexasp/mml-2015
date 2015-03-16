package communication.peer.behaviours;

import communication.BehaviorFactory;
import communication.PeerAgent;
import communication.messaging.Message;
import communication.messaging.MessageFacade;
import jade.core.behaviours.Behaviour;
import learning.Model;
import org.junit.Before;
import org.junit.Test;
import testutils.LambdaMatcher;

import static org.mockito.Mockito.*;

/**
 * Created by alex on 3/9/15.
 */
public class PeerUpdateBehaviorTest {

    private PeerUpdateBehavior _updateBehavior;
    private MessageFacade _messageFacade;
    private PeerAgent _peerAgent;
    private BehaviorFactory _behaviorFactory;

    @Before
    public void setUp(){
        _messageFacade = mock(MessageFacade.class);
        _peerAgent = mock(PeerAgent.class);
        _behaviorFactory = mock(BehaviorFactory.class);

        _updateBehavior = new PeerUpdateBehavior(_peerAgent, _messageFacade, _behaviorFactory);
    }

    private Message fakeMessage(Model model) {
        Message message = mock(Message.class);
        when(_messageFacade.hasMessage()).thenReturn(true);
        when(_messageFacade.nextMessage()).thenReturn(message);
        when(message.getModel()).thenReturn(model);
        return message;
    }


    @Test
    public void action_NewMessage_AddsMessageToModelPool(){
        Model model = mock(Model.class);
        Message message = fakeMessage(model);

        _updateBehavior.action();

        verify(_peerAgent).addModel(model);
    }

    @Test
    public void action_NewMessage_AddsModelPropegatingBehavior(){
        Model model = mock(Model.class);
        fakeMessage(model);
        Behaviour propegateBehavior = mock(PropegateBehavior.class);
        when(_behaviorFactory.getModelPropegate(_peerAgent, model)).thenReturn(propegateBehavior);

        _updateBehavior.action();

        verify(_peerAgent).addBehaviour(propegateBehavior);
    }

    @Test
    public void action_NoNewMessage_DoesNotFetchMessage(){
        when(_messageFacade.hasMessage()).thenReturn(false);

        _updateBehavior.action();

        verify(_messageFacade, never()).nextMessage();
    }

}
