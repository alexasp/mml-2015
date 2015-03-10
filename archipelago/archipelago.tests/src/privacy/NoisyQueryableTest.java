package privacy;

import org.junit.Before;
import org.junit.Test;
import privacy.math.NoiseGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/5/15.
 */
public class NoisyQueryableTest {


    private NoisyQueryable _queryable;
    private Collection<Double> _data;
    private NoiseGenerator _noiseGenerator;
    private BudgetedAgent _agent;

    @Before
    public void PinqQueryable() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        _data = new ArrayList<Double>();
        _agent = mock(BudgetedAgent.class);
        when(_agent.getEpsilon()).thenReturn(1.0);

        _noiseGenerator = mock(NoiseGenerator.class);

        _queryable = breakConstructorPrivacy(_agent, _data, _noiseGenerator);
    }

    private NoisyQueryable breakConstructorPrivacy(BudgetedAgent agent, Collection<Double> data, NoiseGenerator noiseGenerator) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<NoisyQueryable> constructor = (Constructor<NoisyQueryable>) NoisyQueryable.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        return constructor.newInstance(_agent, _data, _noiseGenerator);
    }

    @Test
    public void constructor_AllPrivate(){
        Constructor<?>[] constructors = NoisyQueryable.class.getDeclaredConstructors();
        for(Constructor constructor : constructors){
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        }
    }

    @Test
    public void project_DoubleToBoolean_HasSameAgent(){
        Function<Double, Boolean> func = x -> x > 0.0;

        NoisyQueryable<Boolean> projection = _queryable.project(func);

        assertEquals(_queryable.getAgent(), projection.getAgent());
    }

    @Test
    public void project_DoubleToBooleanNoNoise_ApproximatelySameSize() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        _queryable = breakConstructorPrivacy(_agent, _data, getNoiseLessNoiseGenerator());
        Function<Double, Boolean> func = x -> x > 0.0;
        _data.addAll(Arrays.asList(5.0, 5.0, 5.0, 5.0));

        NoisyQueryable<Boolean> projection = _queryable.project(func);

        assertEquals(_queryable.count(_agent.getEpsilon() * 0.1), projection.count(_agent.getEpsilon() * 0.1), 0.001);
    }

    @Test
    public void count_AddsLaplaceNoise(){
        _data.addAll(Arrays.asList(3.0, 4.0, 5.0));
        when(_noiseGenerator.fromLaplacian(anyDouble())).thenReturn(-2.0);

        assertEquals(1.0, _queryable.count(_agent.getEpsilon() * 0.1), 0.001);
    }

    @Test(expected = IllegalStateException.class)
    public void count_AgentBudgetTooLow_ThrowsException(){
        when(_agent.getEpsilon()).thenReturn(0.0);

        _queryable.count(1.0);
    }

    @Test
    public void count_AgentBudgetEnough_AppliesCostToAgent(){
        double cost = 0.5;
        when(_agent.getEpsilon()).thenReturn(1.0);

        _queryable.count(cost);

        verify(_agent).apply(cost);
    }

    @Test
    public void sum_NoNoise_ReturnsCorrectSum(){

    }

    private NoiseGenerator getNoiseLessNoiseGenerator() {
        NoiseGenerator generator = mock(NoiseGenerator.class);
        return generator;
    }

}
