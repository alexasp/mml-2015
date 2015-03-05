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
    private PinqAgent _agent;
    private NoiseGenerator _noiseGenerator;

    public NoisyQueryable(PinqAgent _agent, Collection<T> data, NoiseGenerator _noiseGenerator) {
        _data = data;
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

    public PinqAgent getAgent() {
        return _agent;
    }

    public long Count() {
        return 0;
    }

    public double sum(double epsilon) {
        return 0.0;
    }
}
