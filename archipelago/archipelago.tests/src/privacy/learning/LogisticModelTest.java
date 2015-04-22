package privacy.learning;

import learning.LabeledSample;
import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;

import java.util.List;
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
public class LogisticModelTest {


    private double _epsilon;
    private LogisticModel _model;
    private List<LabeledSample> _data;
    private LogisticModel _logisticModel;

    @Before
    public void setUp(){
        _epsilon = 1.0;

        _logisticModel = mock(LogisticModel.class);
        _data = mock(List.class);
    }

    

}
