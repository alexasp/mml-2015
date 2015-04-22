package communication.peer.behaviours;

import communication.PeerAgent;
import communication.messaging.MessageFacade;
import communication.peer.AggregationPerformative;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.MessageTemplate;
import learning.Model;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ModelAggregationBehaviorTest {


    private ModelAggregationBehavior _aggregationBehavior;
    private MessageFacade _messaging;
    private Model _model;
    private PeerAgent _peerAgent;

    @Before
    public void setUp() {
        _messaging = mock(MessageFacade.class);
        _model = mock(Model.class);
        _peerAgent = mock(PeerAgent.class);
        when(_peerAgent.getLocalModel()).thenReturn(_model);

        _aggregationBehavior = new ModelAggregationBehavior(_peerAgent, _messaging);
    }

    @Test
    public void isCyclic(){
        assertTrue(_aggregationBehavior instanceof CyclicBehaviour);
    }

    @Test
    public void action_RequestsAggregationGroup(){
        _aggregationBehavior.action();

        verify(_messaging).requestAggregationGroup();
    }

    @Test
    public void action_GroupConfirmationAndIsNotFirstAgent_SendsLocalModelToFirstAgent(){
        when(_messaging.hasMessage(AggregationPerformative.GroupFormation.ordinal())).thenReturn(true);
        AID curator = mock(AID.class);
        List<AID> agents = Arrays.asList(curator, mock(AID.class));
        when(_messaging.nextGroupMessage()).thenReturn(agents);

        _aggregationBehavior.action();

        verify(_messaging).sendToPeer(curator, _model, AggregationPerformative.ModelContribution);
    }

}