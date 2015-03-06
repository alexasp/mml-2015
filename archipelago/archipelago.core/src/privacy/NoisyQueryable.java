package privacy;

import privacy.math.NoiseGenerator;

import java.util.ArrayList;
import java.util.Collection;
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

    public double sum(double epsilon) {
        return 0.0;
    }
}
