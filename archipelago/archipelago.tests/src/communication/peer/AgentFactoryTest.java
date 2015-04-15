package communication.peer;

import communication.BehaviourFactory;
import communication.Environment;
import communication.PeerAgent;
import communication.messaging.MessageFacadeFactory;
import communication.messaging.PeerGraph;
import experiment.DataLoader;
import learning.LabeledSample;
import org.junit.Before;
import org.junit.Test;
import privacy.Budget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/10/15.
 */
public class AgentFactoryTest {

    private AgentFactory _peerFactory;

    int _parts;
    List<LabeledSample> _data;
    private BehaviourFactory _behaviourFactory;
    private List<LabeledSample> _partition1;
    private List<LabeledSample> _partition2;
    private MessageFacadeFactory _messageFacadeFactory;
    private int _iterations = 5;
    private PeerGraph _peerGraph;
    private DataLoader _dataLoader;
    private double _budget = 2.0;
    private Environment _environment;
    private int _parameters = 20;

    @Before
    public void setUp(){
        _parts = 2;

        _dataLoader = mock(DataLoader.class);
        mockData();

        _peerGraph = mock(PeerGraph.class);
        _behaviourFactory = mock(BehaviourFactory.class);
        _messageFacadeFactory = mock(MessageFacadeFactory.class);
        _environment = mock(Environment.class);
        PeerAgentTest.stubBehaviourFactory(_behaviourFactory, _parameters);
        PeerAgentTest.stubMessageFacadeFactory(_messageFacadeFactory);

        _peerFactory = new AgentFactory(_behaviourFactory, _messageFacadeFactory, _peerGraph, _dataLoader);
    }

    private void mockData() {
        _data = mock(List.class);
        _partition1 = mock(List.class);
        _partition2 = mock(List.class);

        List<List<LabeledSample>> partitioned = new ArrayList<>(Arrays.asList(_partition1, _partition2));
        when(_dataLoader.partition(anyInt(), same(_data))).thenReturn(partitioned);
    }


    @Test
    public void createPeers_OnePeerPerPartition(){
        List<PeerAgent> peers = _peerFactory.createPeers(_data, _parts, _iterations, _budget, _parameters, 0);

        assertEquals(_parts, peers.size());
    }

    @Test
    public void createPeers_PartitionsData(){
        List<PeerAgent> peers = _peerFactory.createPeers(_data, _parts, _iterations, _budget, _parameters, 0);

        assertEquals(_partition1, peers.get(0).getData());
        assertEquals(_partition2, peers.get(1).getData());
        assertEquals(peers.get(0).getIterations(), _iterations);
        assertEquals(peers.get(0).getParameterLength(), _parameters);
    }

}
