package privacy.learning;

import learning.LabeledExample;
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
    private NoisyQueryable<LabeledExample> _queryable;
    private BudgetedAgent _agent;

    @Before
    public void setUp(){
        _epsilon = 1.0;

        _agent = mock(BudgetedAgent.class);
        _queryable = mock(NoisyQueryable.class);
        _model = new DifferentialLogisticModel();
    }


    @Test
    public void step_WithData_UpdatesByNoisySum(){
        NoisyQueryable<Double> errors = mock(NoisyQueryable.class);
        when(_queryable.project(DifferentialLogisticModel::errorProjection)).thenReturn(errors);

        _model.step(_epsilon, _queryable);

        fail("Make this test require a very specific, correct projection");
    }

    @Test
    public void errorProjection_GetsExampleError(){
        LabeledExample example = mock(LabeledExample.class);
        when(example.getLabel()).thenReturn(-1.0);
        when(example.getFeatures()).thenReturn(new double[]{2.0, 3.0, 4.0});

        Double error = _model.errorProjection(example);
    }

}
