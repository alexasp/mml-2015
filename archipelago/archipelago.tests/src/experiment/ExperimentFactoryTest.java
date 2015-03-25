package experiment;

import communication.Environment;
import communication.peer.PeerFactory;
import jade.wrapper.StaleProxyException;
import learning.LabeledSample;
import learning.metrics.PerformanceMetrics;
import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aspis on 25.03.2015.
 */
public class ExperimentFactoryTest {

    private ExperimentFactory _experimentFactory;
    private PeerFactory _peerFactory;
    private PerformanceMetrics _performanceMetrics;
    private Environment _environment;

    @Before
    public void setUp(){

        _peerFactory = mock(PeerFactory.class);
        _performanceMetrics = mock(PerformanceMetrics.class);
        _environment = mock(Environment.class);
        _experimentFactory = new ExperimentFactory(_peerFactory, _performanceMetrics, _environment);
    }

    @Test
    public void getExperiment_ConfiguresExperiment() throws StaleProxyException {
        NoisyQueryable<LabeledSample> samples = mock(NoisyQueryable.class);
        when(samples.partition(anyDouble())).thenReturn(Arrays.asList(mock(NoisyQueryable.class), mock(NoisyQueryable.class)));
        double trainRatio = 0.5;
        int peerCount = 10;
        double testCost = 0.1;
        int iterations = 10;

        Experiment experiment = _experimentFactory.getExperiment(samples, trainRatio, peerCount, testCost, iterations);

        assertEquals(samples, experiment.getData());
        assertEquals(trainRatio, experiment.getTrainRatio(), 0.0001d);
        assertEquals(peerCount, experiment.getPeerCount());
        assertEquals(testCost, experiment.getTestCost(), 0.0001d);
        assertEquals(iterations, experiment.getIterations());
    }

}
