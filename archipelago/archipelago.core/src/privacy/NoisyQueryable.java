package privacy;

import learning.LabeledSample;
import privacy.math.NoiseGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Created by alex on 3/5/15.
 */
public class NoisyQueryable<T> {

    private Collection<T> _data;
    private BudgetedAgent _agent;
    private NoiseGenerator _noiseGenerator;

    private NoisyQueryable(BudgetedAgent agent, Collection<T> data, NoiseGenerator noiseGenerator) {
        _data = data;
        _agent = agent;
        _noiseGenerator = noiseGenerator;
    }

    public <Y> NoisyQueryable<Y> project(Function<T, Y> projection) {
        Collection<Y> values = newDataContainer();

        for(T record : _data){
            values.add(projection.apply(record));
        }

        return new NoisyQueryable<Y>(_agent, values, _noiseGenerator);
    }

    private <NewType> Collection<NewType> newDataContainer() {
        return new ArrayList<NewType>();
    }

    public BudgetedAgent getAgent() {
        return _agent;
    }

    public double count(double epsilon) {
        if(_agent.getEpsilon() >= epsilon){
            _agent.apply(epsilon);
            return ((double)_data.size()) + _noiseGenerator.fromLaplacian(1.0 / epsilon);
        } else {
            throw new IllegalStateException("Agent disclosure budget too low for query.");
        }
    }

    /**
     * Clamps output values of projection to [-1.0, +1.0]
     * @param epsilon
     * @param projection
     * @return
     */
    public double sum(double epsilon, Function<T, Double> projection) {
        return _data.stream().mapToDouble(record -> projection.apply(record))
                .map(num -> num > 1.0 ? 1.0 : num)
                .map(num -> num < -1.0 ? -1.0 : num)
                .sum() + _noiseGenerator.fromLaplacian(1.0 / epsilon);
    }

    public List<NoisyQueryable<LabeledSample>> partition(int parts) {
        throw new UnsupportedOperationException();
    }

    public List<NoisyQueryable<LabeledSample>> partition(double trainRatio) {
        return null;
    }
}
