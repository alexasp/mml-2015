package privacy;

import org.junit.Before;
import org.junit.Test;
import privacy.math.NoiseGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/5/15.
 */
public class NoisyQueryableTest {


    private NoisyQueryable _queryable;
    private Collection<Double> _data;
    private PinqAgent _agent;
    private NoiseGenerator _noiseGenerator;

    @Before
    public void PinqQueryable(){
        _data = new ArrayList<Double>();
        _agent = mock(PinqAgent.class);
        when(_agent.getEpsilon()).thenReturn(1.0);

        _noiseGenerator = mock(NoiseGenerator.class);

        _queryable = new NoisyQueryable<Double>(_agent, _data, _noiseGenerator);
    }

    @Test
    public void project_DoubleToBoolean_HasSameAgent(){
        Function<Double, Boolean> func = x -> x > 0.0;

        NoisyQueryable<Boolean> projection = _queryable.project(func);

        assertEquals(_queryable.getAgent(), projection.getAgent());
    }

    @Test
    public void project_DoubleToBooleanNoNoise_ApproximatelySameSize(){
        _queryable = new NoisyQueryable(_agent, _data, getNoiseLessNoiseGenerator());
        Function<Double, Boolean> func = x -> x > 0.0;
        _data.addAll(Arrays.asList(5.0, 5.0, 5.0, 5.0));

        NoisyQueryable<Boolean> projection = _queryable.project(func);

        assertEquals(_queryable.Count(_agent.getEpsilon()*0.1), projection.Count(_agent.getEpsilon()*0.1), 0.001);
    }

    @Test
    public void count_AddsLaplaceNoise(){
        _data.addAll(Arrays.asList(3.0, 4.0, 5.0));
        when(_noiseGenerator.fromLaplacian(anyDouble())).thenReturn(-2.0);

        assertEquals(1.0, _queryable.Count(_agent.getEpsilon()*0.1), 0.001);
    }


    private NoiseGenerator getNoiseLessNoiseGenerator() {
        NoiseGenerator generator = mock(NoiseGenerator.class);
        return generator;
    }

}
