package experiment;

import communication.Environment;
import communication.PeerAgent;
import communication.peer.PeerFactory;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import learning.LabeledSample;
import learning.metrics.PerformanceMetrics;
import org.apache.commons.math3.analysis.function.Exp;
import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
    private PerformanceMetrics _performanceMetrics;
    private double _testCost = 0.1;
    private int _iterations;
    private Environment _environment;
    private int iterations = 5;

    @Before
    public void setUp(){
        _samples = mock(NoisyQueryable.class);
        _peerFactory = mock(PeerFactory.class);
        _performanceMetrics = mock(PerformanceMetrics.class);
        _environment = mock(Environment.class);
        _trainRatio = 0.8;
        _iterations = 5;

        _train = mock(NoisyQueryable.class);
        _test = mock(NoisyQueryable.class);
        parts = Arrays.asList(_train, _test);
        when(_samples.partition(_trainRatio)).thenReturn(parts);
    }

    @Test
    public void construct_CreatesCorrectPeerCountAndGivesIterations() throws StaleProxyException {
        int peerCount = 3;

        new Experiment(_samples, _trainRatio, peerCount, _peerFactory, _performanceMetrics, _testCost, _environment, iterations);

        verify(_peerFactory).createPeers(any(NoisyQueryable.class), eq(peerCount), eq(_iterations));
    }

    @Test
    public void construct_registersPeerWithRunEnvironment() throws StaleProxyException {
        PeerAgent agent1 = mock(PeerAgent.class);
        PeerAgent agent2 = mock(PeerAgent.class);
        when(_peerFactory.createPeers(_train, _peerCount, _iterations)).thenReturn(Arrays.asList(agent1, agent2));

        new Experiment(_samples, _trainRatio, _peerCount, _peerFactory, _performanceMetrics, _testCost, _environment, iterations);

        verify(_environment).registerAgent(agent1);
        verify(_environment).registerAgent(agent2);
    }

    @Test
    public void construct_GivesPeersTrainingData() throws StaleProxyException {
        new Experiment(_samples, _trainRatio, _peerCount, _peerFactory, _performanceMetrics, _testCost, _environment, iterations);

        verify(_peerFactory).createPeers(_train, _peerCount, _iterations);
    }


    @Test
    public void run_RunsEnvironment() throws ControllerException {
        int iterations = 10;
        int peers = 2;
        PeerAgent agent1 = mock(PeerAgent.class);
        PeerAgent agent2 = mock(PeerAgent.class);
        when(_peerFactory.createPeers(_train, peers, iterations)).thenReturn(Arrays.asList(agent1, agent2));
        Consumer<Experiment> completionListener = mock(Consumer.class);

        Experiment experiment = new Experiment(_samples, _trainRatio, _peerCount, _peerFactory, _performanceMetrics, _testCost, _environment, iterations);
        experiment.run(completionListener);

        verify(completionListener).accept(experiment);
        fail("This test should verify that the completionlisteningagent is registered");
    }

    @Test
    public void test_GetsResultForEachAgent() throws StaleProxyException {
        int peers = 2;
        PeerAgent agent1 = mock(PeerAgent.class);
        PeerAgent agent2 = mock(PeerAgent.class);
        when(_peerFactory.createPeers(_train, peers, _iterations)).thenReturn(Arrays.asList(agent1, agent2));
        when(agent1.labelData(_test)).thenReturn(mock(List.class));
        when(agent2.labelData(_test)).thenReturn(mock(List.class));
        when(_performanceMetrics.errorRate(same(_test), any(List.class), eq(_testCost))).thenReturn(0.15);

        Experiment experiment = new Experiment(_samples, _trainRatio, _peerCount, _peerFactory, _performanceMetrics, _testCost, _environment, iterations);
        List<Double> errors = experiment.test();

        assertEquals(errors.get(0), 0.15, 0.0001d);
        assertEquals(errors.get(1), 0.15, 0.0001d);
        assertEquals(errors.size(), 2);
    }

}
