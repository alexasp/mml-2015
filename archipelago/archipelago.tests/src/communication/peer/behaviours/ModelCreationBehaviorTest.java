package communication.peer.behaviours;

import communication.PeerAgent;
import learning.LabeledSample;
import learning.ModelFactory;
import learning.models.LogisticModel;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;
import privacy.math.RandomGenerator;
import testutils.LambdaMatcher;

import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/23/15.
 */
public class ModelCreationBehaviorTest {

    private ModelCreationBehavior _creationBehaviour;
    private PeerAgent _agent;
    private ModelFactory _modelFactory;
    private LogisticModel _model;
    private NoisyQueryable<LabeledSample> _queryable;
    private int _parameters = 3;
    private RandomGenerator _randomGenerator;

    @Before
    public void setUp(){
        _agent = mock(PeerAgent.class);
        _modelFactory = mock(ModelFactory.class);
        _model = mock(LogisticModel.class);
        _randomGenerator = mock(RandomGenerator.class);
        when(_randomGenerator.uniform(-1.0, 1.0)).thenReturn(0.1, 0.2, 0.3);

        Predicate<double[]> predicate = x -> {assertEquals(0.1, x[0], 0.0001d); assertEquals(0.2, x[1], 0.0001d); assertEquals(0.3, x[2], 0.0001d); return true;};
                when(_modelFactory.getLogisticModel(org.mockito.Matchers.argThat(new LambdaMatcher<>(predicate)))).thenReturn(_model);
        _queryable = mock(NoisyQueryable.class);

        when(_agent.getData()).thenReturn(_queryable);

        _creationBehaviour = new ModelCreationBehavior(_agent, _modelFactory, _randomGenerator, _parameters);
    }

    @Test
    public void action_createsInitialModel(){
        _creationBehaviour.action();

        verify(_model).update(_agent.getUpdateCost(), _queryable);
        verify(_agent).addModel(_model);
    }

}
