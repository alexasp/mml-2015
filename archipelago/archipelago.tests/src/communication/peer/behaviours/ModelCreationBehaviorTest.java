package communication.peer.behaviours;

import communication.BehaviourFactory;
import communication.PeerAgent;
import jade.core.behaviours.Behaviour;
import learning.LabeledSample;
import learning.LogisticModelFactory;
import learning.ModelFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import privacy.NoisyQueryable;
import privacy.learning.DifferentialLogisticModel;
import privacy.math.RandomGenerator;
import testutils.LambdaMatcher;

import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
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
    private DifferentialLogisticModel _model;
    private NoisyQueryable<LabeledSample> _queryable;
    private int _parameters = 3;
    private RandomGenerator _randomGenerator;
    private BehaviourFactory _behaviorFactory;
    private Behaviour _propegateBehavior;

    @Before
    public void setUp() {
        _behaviorFactory = mock(BehaviourFactory.class);
        _agent = mock(PeerAgent.class);
        _modelFactory = mock(ModelFactory.class);
        _model = mock(DifferentialLogisticModel.class);
        _randomGenerator = mock(RandomGenerator.class);
        when(_randomGenerator.uniform(-1.0, 1.0)).thenReturn(0.1, 0.2, 0.3);
        when(_behaviorFactory.getModelPropegate(_agent, _model)).thenReturn(_propegateBehavior);

        Predicate<double[]> predicate = x -> {
            assertEquals(0.1, x[0], 0.0001d);
            assertEquals(0.2, x[1], 0.0001d);
            assertEquals(0.3, x[2], 0.0001d);
            return true;
        };
        when(_modelFactory.getModel(Matchers.argThat(new LambdaMatcher<>(predicate)))).thenReturn(_model);
        _queryable = mock(NoisyQueryable.class);

        when(_agent.getData()).thenReturn(_queryable);

        _creationBehaviour = new ModelCreationBehavior(_agent, _modelFactory, _randomGenerator, _behaviorFactory, _parameters);
    }

    @Test
    public void action_createsInitialModel(){
        _creationBehaviour.action();

        verify(_model).update(_agent.getUpdateCost(), _queryable);
        verify(_agent).addModel(_model);
    }

    @Test
    public void action_addsPropegateBehavior() {
        _creationBehaviour.action();

        verify(_agent).addBehaviour(_propegateBehavior);
    }

}
