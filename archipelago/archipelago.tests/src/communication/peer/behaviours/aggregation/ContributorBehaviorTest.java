package communication.peer.behaviours.aggregation;

import communication.PeerAgent;
import communication.messaging.Message;
import communication.messaging.MessageFacade;
import communication.peer.ArchipelagoPerformatives;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import learning.ParametricModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ContributorBehaviorTest {

    private AID _curator;
    private ContributorBehavior _contributorBehavior;
    private MessageFacade _messageFacade;
    private PeerAgent _peerAgent;
    private ParametricModel _model;
    private String _conversationId;

    @Before
    public void setUp() {
        _curator = mock(AID.class);
        _messageFacade = mock(MessageFacade.class);
        _peerAgent = mock(PeerAgent.class);
        _model = mock(ParametricModel.class);
        when(_peerAgent.getLocalModel()).thenReturn(_model);
        _contributorBehavior = new ContributorBehavior(_peerAgent, _curator, _conversationId, _messageFacade);

        when(_messageFacade.hasMessage(any(ArchipelagoPerformatives.class))).thenReturn(false);
    }

    @Test
    public void isType(){
        assertTrue(CyclicBehaviour.class.isAssignableFrom(ContributorBehavior.class));
    }

    @Test
    public void action_SendsLocalModelToCuratorOnlyOnce() {
        _contributorBehavior.action();

        verify(_messageFacade, times(1)).sendToPeer(_curator, _model, ArchipelagoPerformatives.ModelContribution, _conversationId);

        _contributorBehavior.action();
        verify(_messageFacade, times(1)).sendToPeer(_curator, _model, ArchipelagoPerformatives.ModelContribution, _conversationId);
    }

    @Test
    public void action_ResultResponse_AddsModel() {
        _contributorBehavior.action();
        when(_messageFacade.hasMessage(ArchipelagoPerformatives.AggregatedResult, _curator, _conversationId)).thenReturn(true);
        when(_messageFacade.nextMessage(ArchipelagoPerformatives.AggregatedResult, _curator, _conversationId)).thenReturn(new Message(_model));

        _contributorBehavior.action();

        verify(_peerAgent).addModel(_model);
        verify(_peerAgent).removeBehaviour(_contributorBehavior);
    }



}