package communication.peer.behaviours;

import communication.BehaviourFactory;
import communication.PeerAgent;
import communication.messaging.Message;
import communication.messaging.MessageFacade;
import learning.ParametricModel;
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
    private BehaviourFactory _behaviourFactory;
    private ParametricModel _model;
    private PropegateBehavior _propegateBehavior;
    private CompletionBehaviour _completionBehavior;
    private ModelAggregationBehavior _modelAggregationBehavior;

    @Before
    public void setUp(){
        _messageFacade = mock(MessageFacade.class);
        _peerAgent = mock(PeerAgent.class);
        _behaviourFactory = mock(BehaviourFactory.class);
        when(_peerAgent.getIterations()).thenReturn(2);

        _propegateBehavior = mock(PropegateBehavior.class);
        _completionBehavior = mock(CompletionBehaviour.class);
        _model = mock(ParametricModel.class);
        when(_behaviourFactory.getModelPropegate(_peerAgent, _model)).thenReturn(_propegateBehavior);
        when(_behaviourFactory.getModelAggregation(_peerAgent, _messageFacade)).thenReturn(_modelAggregationBehavior);

        _updateBehavior = new PeerUpdateBehavior(_peerAgent, _messageFacade, _behaviourFactory);
    }

    private Message fakeMessage(ParametricModel model) {
        Message message = mock(Message.class);
        when(_messageFacade.hasMessage(PeerUpdateBehavior.Performative)).thenReturn(true);
        when(_messageFacade.nextMessage(PeerUpdateBehavior.Performative)).thenReturn(message);
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
    public void action_IterationsNotFinished_AddsModelAggregationBehavior(){
        _updateBehavior.action();
        verify(_peerAgent).addBehaviour(_modelAggregationBehavior);
    }

    @Test
    public void action_IterationsFinished_DoesNotAddModelAggregationBehavior(){
        _updateBehavior.action();
        _updateBehavior.action();
        verify(_peerAgent, times(2)).addBehaviour(_modelAggregationBehavior);

        _updateBehavior.action();

        verify(_peerAgent, times(2)).addBehaviour(_modelAggregationBehavior);
    }

    @Test
    public void action_NoNewMessage_DoesNotFetchMessage(){
        when(_messageFacade.hasMessage(PeerUpdateBehavior.Performative)).thenReturn(false);

        _updateBehavior.action();

        verify(_messageFacade, never()).nextMessage(PeerUpdateBehavior.Performative);
    }

}
