package learning;

import java.util.function.Function;

/**
 * Created by alex on 4/9/15.
 */
public interface IQueryable<T> {

    public <Y> IQueryable<Y> project(Function<T, Y> projection);

    public double sum(double epsilon, Function<T, Double> projection);

    double count(double epsilon);
}
