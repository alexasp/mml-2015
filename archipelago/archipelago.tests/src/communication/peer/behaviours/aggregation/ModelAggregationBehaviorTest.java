package communication.peer.behaviours.aggregation;

import communication.BehaviourFactory;
import communication.PeerAgent;
import communication.messaging.MessageFacade;
import communication.peer.AggregationPerformative;
import communication.peer.behaviours.ModelAggregationBehavior;
import communication.peer.behaviours.aggregation.ContributorBehavior;
import communication.peer.behaviours.aggregation.CuratorBehavior;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import learning.Model;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PeerAgent.class, AID.class})
public class ModelAggregationBehaviorTest {


    private ModelAggregationBehavior _aggregationBehavior;
    private MessageFacade _messaging;
    private Model _model;
    private PeerAgent _peerAgent;
    private ContributorBehavior _contributorBehavior;
    private BehaviourFactory _behaviorFactory;
    private AID _aid;
    private CuratorBehavior _curatorBehavior;

    @Before
    public void setUp() {
        studBehaviorFactory();

        _messaging = mock(MessageFacade.class);
        _model = mock(Model.class);
        _peerAgent = PowerMockito.mock(PeerAgent.class);
        _aid = PowerMockito.mock(AID.class);
        when(_peerAgent.getAID()).thenReturn(_aid);
        when(_peerAgent.getLocalModel()).thenReturn(_model);

        _aggregationBehavior = new ModelAggregationBehavior(_peerAgent, _messaging, _behaviorFactory);
    }

    private void studBehaviorFactory() {
        _behaviorFactory = mock(BehaviourFactory.class);
        _contributorBehavior = mock(ContributorBehavior.class);
        _curatorBehavior = mock(CuratorBehavior.class);
        when(_behaviorFactory.getContributorBehavior(any(PeerAgent.class), any(AID.class))).thenReturn(_contributorBehavior);
        when(_behaviorFactory.getCuratorBehavior()).thenReturn(_curatorBehavior);
    }

    @Test
    public void isType() {
        assertTrue(_aggregationBehavior instanceof OneShotBehaviour);
    }

    @Test
    public void action_RequestsAggregationGroup() {
        _aggregationBehavior.action();

        verify(_messaging).requestAggregationGroup();
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
        List<AID> agents = Arrays.asList(_aid, PowerMockito.mock(AID.class));
        setUpGroupConfirmationMessage(agents);

        _aggregationBehavior.action();

        verify(_peerAgent).addBehaviour(_curatorBehavior);
    }

    private void setUpGroupConfirmationMessage(List<AID> agents) {
        when(_messaging.hasMessage(AggregationPerformative.GroupFormation.ordinal())).thenReturn(true);
        when(_messaging.nextGroupMessage()).thenReturn(agents);
    }

}