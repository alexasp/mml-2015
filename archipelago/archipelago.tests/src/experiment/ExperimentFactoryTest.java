package experiment;

import communication.Environment;
import communication.peer.AgentFactory;
import jade.wrapper.StaleProxyException;
import learning.LabeledSample;
import learning.metrics.PerformanceMetrics;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aspis on 25.03.2015.
 */
public class ExperimentFactoryTest {

    private ExperimentFactory _experimentFactory;
    private AgentFactory _peerFactory;
    private PerformanceMetrics _performanceMetrics;
    private Environment _environment;
    private DataLoader _dataLoader;

    @Before
    public void setUp(){

        _peerFactory = mock(AgentFactory.class);
        _performanceMetrics = mock(PerformanceMetrics.class);
        _environment = mock(Environment.class);
        _dataLoader = mock(DataLoader.class);

        _experimentFactory = new ExperimentFactory(_peerFactory, _performanceMetrics, _environment, _dataLoader);
    }

    @Test
    public void getExperiment_ConfiguresExperiment() throws StaleProxyException {
        List<LabeledSample> samples = mock(List.class);
        when(_dataLoader.partition(anyDouble(), same(samples))).thenReturn(Arrays.asList(mock(List.class), mock(List.class)));
        double trainRatio = 0.5;
        int peerCount = 10;
        double testCost = 0.1;
        int iterations = 10;

        Experiment experiment = _experimentFactory.getExperiment(samples, trainRatio, peerCount, testCost, iterations, 2.0);

        assertEquals(samples, experiment.getData());
        assertEquals(trainRatio, experiment.getTrainRatio(), 0.0001d);
        assertEquals(peerCount, experiment.getPeerCount());
        assertEquals(testCost, experiment.getTestCost(), 0.0001d);
        assertEquals(iterations, experiment.getIterations());
    }

}
