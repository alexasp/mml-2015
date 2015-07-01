package privacy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by alex on 3/6/15.
 */


public class BudgetTest {
    private Budget _budget;

    @Test(expected = IllegalStateException.class)
    public void apply_NoBudget_ThrowsException(){
        //ARRANGE
        _budget = new Budget(0.0);

        //ACT
        _budget.apply(1.0);

        //ASSERT
    }
    @Test(expected = IllegalStateException.class)
    public void apply_TooSmallBudget_ThrowsException(){
        _budget = new Budget(0.5);

        //ACT
        _budget.apply(1.0);
    }

    @Test
    public void apply_EnoughBudget_SubtractsCost(){
        double budget = 1.0;
        _budget = new Budget(budget);
        double cost =0.5;

        _budget.apply(cost);

        assertEquals(budget - cost, _budget.getEpsilon(), 0.00001);
    }

}
