package communication.peer.behaviours;

import communication.BehaviourFactory;
import communication.PeerAgent;
import jade.core.behaviours.Behaviour;
import learning.LabeledSample;
import learning.ModelFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import privacy.learning.LogisticModel;
import privacy.math.RandomGenerator;
import testutils.LambdaMatcher;

import java.util.List;
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
    private LogisticModel _model;
    private List<LabeledSample> _data;
    private int _parameters = 3;
    private RandomGenerator _randomGenerator;
    private BehaviourFactory _behaviorFactory;
    private Behaviour _propegateBehavior;

    @Before
    public void setUp() {
        _behaviorFactory = mock(BehaviourFactory.class);
        _agent = mock(PeerAgent.class);
        _modelFactory = mock(ModelFactory.class);
        _model = mock(LogisticModel.class);
        _randomGenerator = mock(RandomGenerator.class);
        when(_behaviorFactory.getModelPropegate(_agent, _model)).thenReturn(_propegateBehavior);

        Predicate<double[]> predicate = x -> {
            assertEquals(0.0, x[0], 0.0001d);
            assertEquals(0.0, x[1], 0.0001d);
            assertEquals(0.0, x[2], 0.0001d);
            return true;
        };
        when(_modelFactory.getModel(Matchers.argThat(new LambdaMatcher<>(predicate)))).thenReturn(_model);
        _data = mock(List.class);

        when(_agent.getData()).thenReturn(_data);

        _creationBehaviour = new ModelCreationBehavior(_agent, _modelFactory, _behaviorFactory, _parameters);
    }

    @Test
    public void action_createsInitialModel(){
        _creationBehaviour.action();

        verify(_model).update(_agent.getUpdateCost(), _data);
        verify(_agent).setLocalModel(_model);
        verify(_agent).addModel(_model);
    }


}
