package experiment;

import communication.PeerAgent;
import communication.peer.PeerFactory;
import learning.LabeledSample;
import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;

import java.util.Arrays;

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
    private Experiment _experiment;
    private int _peers = 2;

    @Before
    public void setUp(){
        _samples = mock(NoisyQueryable.class);
        _peerFactory = mock(PeerFactory.class);

        _experiment = new Experiment(_samples, _peers, _peerFactory);
    }

    @Test
    public void construct_CreatesCorrectPeerCount(){
        int peers = 3;

        new Experiment(any(NoisyQueryable.class), eq(peers), same(_peerFactory));

        verify(_peerFactory).createPeers(_samples, peers);
    }

    @Test
    public void construct_GivesPeersTrainingData(){
        int peers = 3;

    }

    @Test
    public void run_RunsPeersWithSpecifiedIterations(){
        int iterations = 10;
        int peers = 2;
        PeerAgent agent1 = mock(PeerAgent.class);
        PeerAgent agent2 = mock(PeerAgent.class);
        when(_peerFactory.createPeers(_samples, peers)).thenReturn(Arrays.asList(agent1, agent2));

        _experiment.run(iterations);

        verify(agent1).run(iterations);
        verify(agent2).run(iterations);
    }


}
