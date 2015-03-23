package communication.peer;

import communication.BehaviorFactory;
import communication.PeerAgent;
import communication.messaging.MessageFacade;
import communication.messaging.MessageFacadeFactory;
import learning.LabeledSample;
import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/10/15.
 */
public class PeerFactoryTest {

    private PeerFactory _peerFactory;

    int _parts;
    NoisyQueryable<LabeledSample> _data;
    private BehaviorFactory _behaviorFactory;
    private NoisyQueryable<LabeledSample> _partition1;
    private NoisyQueryable<LabeledSample> _partition2;
    private NoisyQueryable<LabeledSample> _partition3;
    private MessageFacadeFactory _messageFacadeFactory;

    @Before
    public void setUp(){
        _parts = 3;
        mockData();

        _behaviorFactory = mock(BehaviorFactory.class);
        _messageFacadeFactory = mock(MessageFacadeFactory.class);
        PeerAgentTest.stubBehaviourFactory(_behaviorFactory);
        PeerAgentTest.stubMessageFacadeFactory(_messageFacadeFactory);

        _peerFactory = new PeerFactory(_behaviorFactory, _messageFacadeFactory);
    }

    private void mockData() {
        _data = mock(NoisyQueryable.class);
        _partition1 = mock(NoisyQueryable.class);
        _partition2 = mock(NoisyQueryable.class);
        _partition3 = mock(NoisyQueryable.class);

        List<NoisyQueryable<LabeledSample>> partitioned = new ArrayList<>(Arrays.asList(_partition1, _partition2, _partition3));
        when(_data.partition(anyInt())).thenReturn(partitioned);
    }


    @Test
    public void createPeers_OnePeerPerPartition(){
        List<PeerAgent> peers = _peerFactory.createPeers(_data, _parts);

        assertEquals(_parts, peers.size());
    }

    @Test
    public void createPeers_PartitionsData(){
        List<PeerAgent> peers = _peerFactory.createPeers(_data, _parts);

        assertEquals(_partition1, peers.get(0).getData());
        assertEquals(_partition2, peers.get(1).getData());
        assertEquals(_partition3, peers.get(2).getData());
    }

}
