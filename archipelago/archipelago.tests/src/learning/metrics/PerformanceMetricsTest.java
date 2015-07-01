package learning.metrics;

import communication.PeerAgent;
import learning.LabeledSample;
import org.junit.Before;
import org.junit.Test;
import privacy.Budget;
import privacy.NoisyQueryable;
import privacy.NoisyQueryableTest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PerformanceMetricsTest {

    private PerformanceMetrics _metrics;
    private Budget _budget;
    private NoisyQueryable _queryable;

    @Before
    public void setUp(){
        _budget = mock(Budget.class);
        _queryable = mock(NoisyQueryable.class);

        _metrics = new PerformanceMetrics();
    }

    @Test
    public void errorRate(){
        List<LabeledSample> samples = Arrays.asList(new LabeledSample(-1.0, new double[0]), new LabeledSample(1.0, new double[0]), new LabeledSample(1.0, new double[0]));
        List<Double> predictions = Arrays.asList(flipLabel(samples.get(0).getLabel()), samples.get(1).getLabel(), samples.get(2).getLabel());

        assertEquals(1.0/3.0, _metrics.errorRate(samples, predictions), 0.00001d);
    }

    private double flipLabel(double label) {
        return -label;
    }

}