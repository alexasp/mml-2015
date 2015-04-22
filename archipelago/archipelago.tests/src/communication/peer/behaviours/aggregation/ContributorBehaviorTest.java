package communication.peer.behaviours.aggregation;

import communication.PeerAgent;
import communication.messaging.MessageFacade;
import communication.peer.AggregationPerformative;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import learning.Model;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ContributorBehaviorTest {

    private AID _curator;
    private ContributorBehavior _contributorBehavior;
    private MessageFacade _messageFacade;
    private PeerAgent _peerAgent;
    private Model _model;

    @Before
    public void setUp() {
        _curator = mock(AID.class);
        _messageFacade = mock(MessageFacade.class);
        _peerAgent = mock(PeerAgent.class);
        _model = mock(Model.class);
        when(_peerAgent.getLocalModel()).thenReturn(_model);
        _contributorBehavior = new ContributorBehavior(_peerAgent, _curator, _messageFacade);
    }

    @Test
    public void isType(){
        assertTrue(CyclicBehaviour.class.isAssignableFrom(ContributorBehavior.class));
    }

    @Test
    public void action_SendsLocalModelToCuratorOnlyOnce() {
        _contributorBehavior.action();

        verify(_messageFacade, times(1)).sendToPeer(_curator, _model, AggregationPerformative.ModelContribution);

        _contributorBehavior.action();
        verify(_messageFacade, times(1)).sendToPeer(_curator, _model, AggregationPerformative.ModelContribution);
    }



}