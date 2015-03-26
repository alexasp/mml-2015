package privacy;

import org.junit.Before;
import org.junit.Test;
import privacy.math.RandomGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
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


    private NoisyQueryable<Double> _queryable;
    private Collection<Double> _data;
    private RandomGenerator _randomGenerator;
    private Budget _agent;
    private double _cost;

    @Before
    public void PinqQueryable() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        _cost = 0.5;
        _data = new ArrayList<>();
        _agent = mock(Budget.class);
        when(_agent.getEpsilon()).thenReturn(1.0);

        _randomGenerator = mock(RandomGenerator.class);

        _queryable = breakConstructorPrivacy(_agent, _data, _randomGenerator);
    }

    public static NoisyQueryable breakConstructorPrivacy(Budget budget, Collection<Double> data, RandomGenerator randomGenerator) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<NoisyQueryable> constructor = (Constructor<NoisyQueryable>) NoisyQueryable.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        return constructor.newInstance(budget, data, randomGenerator);
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
    public void projectIndexed_DoubleToBooleanNoNoise_SameSize() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        _queryable = breakConstructorPrivacy(_agent, _data, getNoiseLessNoiseGenerator());
        BiFunction<Double, Integer, Boolean> func = (x, i) -> x > 0.0;
        _data.addAll(Arrays.asList(5.0, 5.0, 5.0, 5.0));

        NoisyQueryable<Boolean> projection = _queryable.projectIndexed(func);

        assertEquals(_queryable.count(_agent.getEpsilon() * 0.1), projection.count(_agent.getEpsilon() * 0.1), 0.001);
    }

    @Test
    public void count_AddsLaplaceNoise(){
        _data.addAll(Arrays.asList(3.0, 4.0, 5.0));
        when(_randomGenerator.fromLaplacian(anyDouble())).thenReturn(-2.0);

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
    public void sum_NoNoise_ReturnsClampedCorrectSum() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        _data.addAll(Arrays.asList(0.5, -0.2, -2.0, 5.0));
        _queryable = breakConstructorPrivacy(_agent, _data, getNoiseLessNoiseGenerator());

        Function<Double,Double> proj = x->x;
        assertEquals(0.3, _queryable.sum(1.0, proj), 0.00001d);
    }

    @Test
    public void sum_NoisyGenerator_AddsNoiseToSum(){
        _data.addAll(Arrays.asList(0.5, -0.2, -2.0, 5.0));
        when(_randomGenerator.fromLaplacian(anyDouble())).thenReturn(5.0);

        Function<Double,Double> proj = x -> x;
        assertEquals(5.3, _queryable.sum(1.0, proj), 0.00001d);
    }

    @Test
    public void noisyAverage_NoNoise_ReturnsAverageOfClampedValues(){

    }

    @Test
    public void noisyAverage_EmptySet_ReturnsUniform(){
        BiFunction<Double, Integer, Double> projection = (x,i) -> (double)i;
        double expectedNoise = -0.023;
        when(_randomGenerator.uniform(-1.0, 1.0)).thenReturn(expectedNoise);

        double average = _queryable.noisyAverage(_cost, projection);

        assertEquals(expectedNoise, average, 0.00001d);
    }

    @Test
    public void noisyAverage_NonEmptySetNoNoise_ReturnsClampedAverage() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Double> data = Arrays.asList(-1.5, -0.5, 4.0, 0.3);
        List<Double> clamped = Arrays.asList(-1.0, -0.5, 1.0, 0.3);
        NoisyQueryable<Double> queryable = breakConstructorPrivacy(_agent, data, getNoiseLessNoiseGenerator());
        BiFunction<Double, Integer, Double> projection = (x,i) -> x;

        double average = queryable.noisyAverage(_cost, projection);

        double expectedAverage = clamped.stream().mapToDouble(Double::doubleValue).sum()/data.size();
        assertEquals(expectedAverage, average, 0.00001d);
    }

    @Test
    public void noisyAverage_NonEmptySet_AddsNoise() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Double> data = Arrays.asList(-1.5, -0.5, 4.0, 0.3);
        List<Double> clamped = Arrays.asList(-1.0, -0.5, 1.0, 0.3);
        BiFunction<Double, Integer, Double> projection = (x,i) -> x;
        NoisyQueryable<Double> queryable = breakConstructorPrivacy(_agent, data, _randomGenerator);
        double noise = -0.01;
        when(_randomGenerator.fromLaplacian(2/_cost)).thenReturn(noise);

        double average = queryable.noisyAverage(_cost, projection);

        double expectedAverage = (clamped.stream().mapToDouble(Double::doubleValue).sum() + noise)/data.size();
        assertEquals(expectedAverage, average, 0.00001d);
    }

    @Test(expected = IllegalStateException.class)
    public void sum_BudgetLow_Throws(){
        when(_agent.getEpsilon()).thenReturn(0.0);

        _queryable.sum(1.0, x -> 2.0);
    }



    public static RandomGenerator getNoiseLessNoiseGenerator() {
        RandomGenerator generator = mock(RandomGenerator.class);
        return generator;
    }

}
