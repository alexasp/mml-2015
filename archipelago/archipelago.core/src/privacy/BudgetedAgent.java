package privacy;

/**
 * Created by alex on 3/5/15.
 */
public class BudgetedAgent {

    double _epsilon;

    public BudgetedAgent(double epsilon) {
        _epsilon = epsilon;
    }

    public double getEpsilon(){
        return _epsilon;
    }

    public void apply(double epsilon) {
        if(epsilon>_epsilon)
            throw new IllegalStateException();
        _epsilon -= epsilon;
    }
}
