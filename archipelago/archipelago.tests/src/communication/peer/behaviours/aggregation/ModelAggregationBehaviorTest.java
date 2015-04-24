package communication.peer.behaviours.aggregation;

import communication.BehaviourFactory;
import communication.PeerAgent;
import communication.messaging.GroupMessage;
import communication.messaging.MessageFacade;
import communication.peer.AggregationPerformative;
import communication.peer.behaviours.ModelAggregationBehavior;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import learning.ParametricModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PeerAgent.class, AID.class})
public class ModelAggregationBehaviorTest {


    private ModelAggregationBehavior _aggregationBehavior;
    private MessageFacade _messageFacade;
    private ParametricModel _model;
    private PeerAgent _peerAgent;
    private ContributorBehavior _contributorBehavior;
    private BehaviourFactory _behaviorFactory;
    private AID _aid;
    private CuratorBehavior _curatorBehavior;
    private String _conversationId = "id";

    @Before
    public void setUp() {
        studBehaviorFactory();

        _messageFacade = mock(MessageFacade.class);
        _model = mock(ParametricModel.class);
        _peerAgent = PowerMockito.mock(PeerAgent.class);
        _aid = PowerMockito.mock(AID.class);
        when(_peerAgent.getAID()).thenReturn(_aid);
        when(_peerAgent.getLocalModel()).thenReturn(_model);

        _aggregationBehavior = new ModelAggregationBehavior(_peerAgent, _messageFacade, _behaviorFactory);
    }

    private void studBehaviorFactory() {
        _behaviorFactory = mock(BehaviourFactory.class);
        _contributorBehavior = mock(ContributorBehavior.class);
        _curatorBehavior = mock(CuratorBehavior.class);
        when(_behaviorFactory.getContributorBehavior(any(PeerAgent.class), any(AID.class), any(MessageFacade.class), eq(_conversationId))).thenReturn(_contributorBehavior);
        when(_behaviorFactory.getCuratorBehavior(anyListOf(AID.class), any(MessageFacade.class), any(PeerAgent.class), eq(_conversationId))).thenReturn(_curatorBehavior);
    }

    @Test
    public void isType() {
        assertTrue(_aggregationBehavior instanceof CyclicBehaviour);
    }


    @Test
    public void action_GroupConfirmationAndIsNotFirstAgent_AddsContributorBehavior() {
        List<AID> agents = Arrays.asList(PowerMockito.mock(AID.class), _aid);
        setUpGroupConfirmationMessage(agents);

        _aggregationBehavior.action();

        verify(_peerAgent).addBehaviour(_contributorBehavior);
    }

    @Test
    public void action_GroupConfirmationAndIsFirstAgent_AddsCuratorBehavior() {
        List<AID> agents = new ArrayList<>(Arrays.asList(_aid, PowerMockito.mock(AID.class)));
        setUpGroupConfirmationMessage(agents);

        _aggregationBehavior.action();

        verify(_peerAgent).addBehaviour(_curatorBehavior);
    }

    private void setUpGroupConfirmationMessage(List<AID> agents) {
        when(_messageFacade.hasMessage(AggregationPerformative.GroupFormation.ordinal())).thenReturn(true);
        GroupMessage message = new GroupMessage(agents, _conversationId);
        when(_messageFacade.nextGroupMessage()).thenReturn(message);
    }

}