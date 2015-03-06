package privacy.learning;

import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;
import privacy.BudgetedAgent;

import java.util.function.Function;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by alex on 3/5/15.
 */
public class DifferentialLogisticModelTest {


    private double _epsilon;
    private DifferentialLogisticModel _model;
    private NoisyQueryable _queryable;
    private BudgetedAgent _agent;

    @Before
    public void setUp(){
        _agent = mock(BudgetedAgent.class);
        _queryable = mock(NoisyQueryable.class);
        _model = new DifferentialLogisticModel();
    }


    @Test
    public void step_WithData_UpdatesByNoisySum(){
        NoisyQueryable<Double> projected = mock(NoisyQueryable.class);
        when(_queryable.project(any(Function.class))).thenReturn(projected);

        _model.step(_epsilon, _queryable);

        verify(_queryable).sum(_agent.getEpsilon());


        fail("Make this test require a very specific, correct projection");
    }

}
