package privacy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by alex on 3/6/15.
 */


public class BudgetedAgentTest {
    private BudgetedAgent _agent;

    @Test(expected = IllegalStateException.class)
    public void apply_NoBudget_ThrowsException(){
        //ARRANGE
        _agent = new BudgetedAgent(0.0);

        //ACT
        _agent.apply(1.0);

        //ASSERT
    }
    @Test(expected = IllegalStateException.class)
    public void apply_TooSmallBudget_ThrowsException(){
        _agent = new BudgetedAgent(0.5);

        //ACT
        _agent.apply(1.0);
    }

    @Test
    public void apply_EnoughBudget_SubtractsCost(){
        double budget = 1.0;
        _agent = new BudgetedAgent(budget);
        double cost =0.5;

        _agent.apply(cost);

        assertEquals(budget - cost,_agent.getEpsilon(),0.00001);
    }

}
