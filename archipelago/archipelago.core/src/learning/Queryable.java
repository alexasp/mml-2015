package learning;

import privacy.NoisyQueryable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by alex on 4/9/15.
 */
public class Queryable<T> implements IQueryable<T> {

    private List<T> _data;

    public Queryable(List<T> data) {
        _data = data;
    }

    @Override
    public <Y> IQueryable<Y> project(Function<T, Y> projection) {
        List<Y> values = newDataContainer();

        for(T record : _data){
            values.add(projection.apply(record));
        }

        return new Queryable<>(values);
    }

    @Override
    public double sum(double epsilon, Function<T, Double> projection) {
        return _data.stream().mapToDouble(record -> projection.apply(record))
                .sum();
    }

    private <NewType> List<NewType> newDataContainer() {
        return new ArrayList<NewType>();
    }
}
