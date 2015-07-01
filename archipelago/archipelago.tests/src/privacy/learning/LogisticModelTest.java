package privacy.learning;

import learning.LabeledSample;
import org.junit.Before;
import java.util.List;
import static org.mockito.Mockito.mock;

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
