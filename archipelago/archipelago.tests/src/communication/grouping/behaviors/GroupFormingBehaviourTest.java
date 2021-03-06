package communication.grouping.behaviors;

import communication.messaging.MessageFacade;
import communication.peer.ArchipelagoPerformatives;
import experiment.ExperimentConfiguration;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import privacy.math.RandomGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Agent.class)
public class GroupFormingBehaviourTest {

    private GroupFormingBehaviour _behaviour;
    private MessageFacade _messageFacade;
    private RandomGenerator _randomGenerator;
    private int _agentCount = 20;
    private List<AID> _agents;
    private Agent _groupAgent;
    private ExperimentConfiguration _configuration;

    @Before
    public void setUp() {
        _groupAgent = PowerMockito.mock(Agent.class);
        when(_groupAgent.getAID()).thenReturn(mock(AID.class));

        _agents = IntStream.range(0, _agentCount).mapToObj(i -> mock(AID.class)).collect(Collectors.toList());

        _messageFacade = mock(MessageFacade.class);
        when(_messageFacade.hasMessage(ArchipelagoPerformatives.AggregationGroupRequest)).thenReturn(true);

        _randomGenerator = mock(RandomGenerator.class);

        _configuration = new ExperimentConfiguration();
        _configuration.aggregations = 2;
        _configuration.groupSize = 3;
        _configuration.updateCost = 0.1d;
        _configuration.epsilon = 0.25d;
        _behaviour = new GroupFormingBehaviour(_groupAgent, new ArrayList<>(_agents), _configuration, _randomGenerator, _messageFacade);
    }

    @Test
    public void isCyclic() {
        assertTrue(CyclicBehaviour.class.isAssignableFrom(GroupFormingBehaviour.class));
    }

    @Test
    public void action_NotFinishedIterations_FormsARandomGroup() {
        int first = 4, second = 2, third = 7;
        when(_randomGenerator.sample(anyList(), eq(_configuration.groupSize)))
                .thenReturn(Arrays.asList(_agents.get(first), _agents.get(second), _agents.get(third)))
                .thenReturn(Arrays.asList(_agents.get(first), _agents.get(second), _agents.get(third)));

        _behaviour.action();
        verify(_messageFacade).publishAggregationGroup(argThat(new MatchesAgentSubset(first, second, third)), anyString());
        _behaviour.action();
        verify(_messageFacade, times(2)).publishAggregationGroup(argThat(new MatchesAgentSubset(first, second, third)), anyString());
    }

    @Test
    public void action_PeerBudgetExpended_IsNotDrawnForNewGroup() {
        _configuration.groupSize = 2;
        when(_randomGenerator.sample(anyList(), eq(_configuration.groupSize)))
                .thenReturn(Arrays.asList(_agents.get(0), _agents.get(2)))
                .thenReturn(Arrays.asList(_agents.get(2), _agents.get(3)));

        _behaviour.action();
        verify(_randomGenerator).sample(anyList(), anyInt());
        verify(_messageFacade).publishAggregationGroup(argThat(new MatchesAgentSubset(0, 2)), anyString());
        _behaviour.action();
        verify(_randomGenerator, times(2)).sample(anyList(), anyInt());
        verify(_messageFacade).publishAggregationGroup(argThat(new MatchesAgentSubset(2, 3)), anyString());
        _behaviour.action();
        verify(_randomGenerator, times(3)).sample(
                argThat(new ArgumentMatcher<List<AID>>() {
                    @Override
                    public boolean matches(Object argument) {
                        List<AID> list = (List<AID>) argument;
                        return !list.contains(_agents.get(2));
                    }
                }),
                eq(_configuration.groupSize));
    }

    @Test
    public void action_NotFinishedIterations_IncrementsId() {
        when(_randomGenerator.uniform(0, _agentCount - 1))
                .thenReturn(1).thenReturn(2).thenReturn(3)
                .thenReturn(4).thenReturn(5).thenReturn(6);

        _behaviour.action();
        verify(_messageFacade).publishAggregationGroup(anyList(), eq("0"));
        _behaviour.action();
        verify(_messageFacade).publishAggregationGroup(anyList(), eq("1"));
    }

    @Test
    public void action_FinishedIterations_RemovesBehavior() {
        int first = 4, second = 2, third = 7;
        when(_randomGenerator.uniform(0, _agentCount))
                .thenReturn(first).thenReturn(second).thenReturn(third)
                .thenReturn(first).thenReturn(second).thenReturn(third);

        _behaviour.action();
        _behaviour.action();

        verify(_groupAgent).removeBehaviour(_behaviour);
    }


    private class MatchesAgentSubset extends ArgumentMatcher<List<AID>> {
        private final int[] _indices;

        public MatchesAgentSubset(int... indices) {
            _indices = indices;
        }

        @Override
        public boolean matches(Object argument) {
            List<AID> list = (List<AID>) argument;
            return IntStream.range(0, _indices.length)
                    .allMatch(i -> list.contains(_agents.get(_indices[i])))
                    && _indices.length == list.size();
        }
    }
}