package privacy.learning;

import learning.LabeledSample;
import learning.models.LogisticModel;
import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by alex on 3/5/15.
 */
public class DifferentialLogisticModelTest {


    private double _epsilon;
    private DifferentialLogisticModel _model;
    private NoisyQueryable<LabeledSample> _queryable;
    private LogisticModel _logisticModel;

    @Before
    public void setUp(){
        _epsilon = 1.0;

        _logisticModel = mock(LogisticModel.class);
        _queryable = mock(NoisyQueryable.class);
        _model = new DifferentialLogisticModel(_logisticModel);
    }


    @Test
    public void step_WithData_UpdatesByProjectingToErrorAndSumming(){
        NoisyQueryable<Double> errors = mock(NoisyQueryable.class);
        when(_queryable.project(any(Function.class))).thenReturn(errors);
        when(_logisticModel.getParameters()).thenReturn(new double[]{2.0, 2.0, 2.0});

        when(errors.sum(eq(_epsilon), any(Function.class))).thenReturn(5.0);
        when(_logisticModel.getDimensionality()).thenReturn(3);

        _model.update(_epsilon, _queryable);

        verify(_logisticModel).gradientUpdate(new double[]{5.0, 5.0, 5.0});
    }

    @Test
    public void errorProjection_GetsExampleError(){
        LabeledSample example = mock(LabeledSample.class);
        when(example.getLabel()).thenReturn(-1.0);
        when(example.getFeatures()).thenReturn(new double[]{2.0, 3.0, 4.0});
        double expectedError = -0.4;
        when(_logisticModel.errorProjection(example)).thenReturn(expectedError);

        assertEquals(expectedError, _model.errorProjection(example), 0.0001d);
    }

}
