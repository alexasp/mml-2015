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
    private Model _model;
    private PropegateBehavior _propegateBehavior;

    @Before
    public void setUp(){
        _messageFacade = mock(MessageFacade.class);
        _peerAgent = mock(PeerAgent.class);
        _behaviorFactory = mock(BehaviorFactory.class);
        when(_peerAgent.getIterations()).thenReturn(2);

        _propegateBehavior = mock(PropegateBehavior.class);
        _model = mock(Model.class);
        when(_behaviorFactory.getModelPropegate(_peerAgent, _model)).thenReturn(_propegateBehavior);


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
        Message message = fakeMessage(_model);

        _updateBehavior.action();

        verify(_peerAgent).addModel(_model);
    }

    @Test
    public void action_NewMessage_AddsModelPropegatingBehavior(){
        fakeMessage(_model);

        _updateBehavior.action();

        verify(_peerAgent).addBehaviour(_propegateBehavior);
    }

    @Test
    public void action_NewMessage_DoesNotAddModelPropegateAfterFinalIteration(){
        fakeMessage(_model);

        when(_peerAgent.getIterations()).thenReturn(2);

        verify(_peerAgent, never()).addBehaviour(_propegateBehavior);
        _updateBehavior.action(); // iteration 0
        verify(_peerAgent, times(1)).addBehaviour(_propegateBehavior);
        _updateBehavior.action(); // final iteration
        verify(_peerAgent, times(1)).addBehaviour(any(PropegateBehavior.class));
    }

    @Test
    public void action_NoNewMessage_DoesNotFetchMessage(){
        when(_messageFacade.hasMessage()).thenReturn(false);

        _updateBehavior.action();

        verify(_messageFacade, never()).nextMessage();
    }

}