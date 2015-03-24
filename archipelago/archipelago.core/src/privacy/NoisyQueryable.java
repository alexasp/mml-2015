package privacy;

import learning.LabeledSample;
import privacy.math.NoiseGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by alex on 3/5/15.
 */
public class NoisyQueryable<T> {

    private List<T> _data;
    private Budget _agent;
    private NoiseGenerator _noiseGenerator;

    private NoisyQueryable(Budget agent, List<T> data, NoiseGenerator noiseGenerator) {
        _data = data;
        _agent = agent;
        _noiseGenerator = noiseGenerator;
    }

    //TODO: Thesa projections allow for counting the exact number of records or exporting the samples. Problem?
    public <Y> NoisyQueryable<Y> project(Function<T, Y> projection) {
        List<Y> values = newDataContainer();

        for(T record : _data){
            values.add(projection.apply(record));
        }

        return new NoisyQueryable<Y>(_agent, values, _noiseGenerator);
    }

    //TODO: Thesa projections allow for counting the exact number of records or exporting the samples. Problem?
    public <Y> NoisyQueryable<Y> projectIndexed(BiFunction<T, Integer, Y> projection) {
        List<Y> values = IntStream.range(0, _data.size())
                .mapToObj(i -> projection.apply(_data.get(i), i))
                .collect(Collectors.toList());
        return new NoisyQueryable<Y>(_agent, values, _noiseGenerator);
    }

    private <NewType> List<NewType> newDataContainer() {
        return new ArrayList<NewType>();
    }

    public Budget getAgent() {
        return _agent;
    }

    public double count(double epsilon) {
        checkAgentBudget(epsilon);

        _agent.apply(epsilon);
        return ((double)_data.size()) + _noiseGenerator.fromLaplacian(1.0 / epsilon);
    }

    public double noisyAverage(BiFunction<T, Integer, Double> projection) {
        throw new UnsupportedOperationException();
    }

    /**
     * Clamps output values of projection to [-1.0, +1.0]
     * @param epsilon
     * @param projection
     * @return
     */
    public double sum(double epsilon, Function<T, Double> projection) {
        checkAgentBudget(epsilon);
        return _data.stream().mapToDouble(record -> projection.apply(record))
                .map(num -> num > 1.0 ? 1.0 : num)
                .map(num -> num < -1.0 ? -1.0 : num)
                .sum() + _noiseGenerator.fromLaplacian(1.0 / epsilon);
    }

    private void checkAgentBudget(double epsilon) {
        if(_agent.getEpsilon() < epsilon){ throw new IllegalStateException("Agent disclosure budget too low for query."); }
    }

    public List<NoisyQueryable<LabeledSample>> partition(int parts) {
        throw new UnsupportedOperationException();
    }

    public List<NoisyQueryable<LabeledSample>> partition(double trainRatio) {
        return null;
    }



}
