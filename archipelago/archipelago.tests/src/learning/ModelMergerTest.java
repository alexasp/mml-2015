package learning;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelMergerTest {


    private ModelMerger _merger;
    private ParametricModel _model1;
    private ParametricModel _model2;
    private ParametricModel _model3;
    private ModelFactory _modelFactory;

    @Before
    public void setUp() {
        _modelFactory = mock(ModelFactory.class);
        _merger = new ModelMerger(_modelFactory);
        _model1 = mock(ParametricModel.class);
        _model2 = mock(ParametricModel.class);
        _model3 = mock(ParametricModel.class);
    }


    @Test
    public void merge_AddsAndAverges(){
        when(_model1.getParameters()).thenReturn(new double[]{1.0, -1.5});
        when(_model2.getParameters()).thenReturn(new double[]{1.0, 2.0});
        double[] expected = new double[] {((1.0+1.0)/2.0), ((-1.5+2.0)/2.0)};
        when(_modelFactory.getModel(argThat(new DoubleArrayMatcher(expected)))).thenReturn(_model3);

        ParametricModel model = _merger.merge(Arrays.asList(_model1, _model2));

        assertEquals(_model3, model);
    }

    private class DoubleArrayMatcher extends ArgumentMatcher<double[]> {

        private final double[] _expected;

        public DoubleArrayMatcher(double[] expected) {
            _expected = expected;
        }

        @Override
        public boolean matches(Object argument) {
            double[] array = (double[])argument;
            boolean match = IntStream.range(0, _expected.length)
                    .allMatch(i -> Math.abs(_expected[i] - array[i]) < 0.0001d);
            return match;
        }
    }
}