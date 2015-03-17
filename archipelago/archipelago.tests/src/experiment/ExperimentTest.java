package experiment;

import communication.PeerAgent;
import communication.peer.PeerFactory;
import learning.LabeledSample;
import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/17/15.
 */
public class ExperimentTest {


    private PeerFactory _peerFactory;
    private NoisyQueryable<LabeledSample> _samples;
    private int _peerCount = 2;
    private double _trainRatio;
    private NoisyQueryable<LabeledSample> _train;
    private NoisyQueryable<LabeledSample> _test;
    private List<NoisyQueryable<LabeledSample>> parts;

    @Before
    public void setUp(){
        _samples = mock(NoisyQueryable.class);
        _peerFactory = mock(PeerFactory.class);
        _trainRatio = 0.8;

        _train = mock(NoisyQueryable.class);
        _test = mock(NoisyQueryable.class);
        parts = Arrays.asList(_train, _test);
        when(_samples.partition(_trainRatio)).thenReturn(parts);
    }

    @Test
    public void construct_CreatesCorrectPeerCount(){
        int peerCount = 3;

        new Experiment(_samples, _trainRatio, peerCount, _peerFactory);

        verify(_peerFactory).createPeers(any(NoisyQueryable.class), eq(peerCount));
    }

    @Test
    public void construct_GivesPeersTrainingData(){


        new Experiment(_samples, _trainRatio, _peerCount, _peerFactory);

        verify(_peerFactory).createPeers(_train, _peerCount);
    }


    @Test
    public void run_RunsPeersWithSpecifiedIterations(){
        int iterations = 10;
        int peers = 2;
        PeerAgent agent1 = mock(PeerAgent.class);
        PeerAgent agent2 = mock(PeerAgent.class);
        when(_peerFactory.createPeers(_train, peers)).thenReturn(Arrays.asList(agent1, agent2));

        new Experiment(_samples, _trainRatio, _peerCount, _peerFactory).run(iterations);

        verify(agent1).run(iterations);
        verify(agent2).run(iterations);
    }


}
