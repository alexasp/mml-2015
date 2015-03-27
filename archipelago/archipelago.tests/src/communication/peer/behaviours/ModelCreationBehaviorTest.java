package communication.peer.behaviours;

import communication.PeerAgent;
import learning.LabeledSample;
import learning.ModelFactory;
import learning.models.LogisticModel;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;
import testutils.LambdaMatcher;

import java.util.function.Predicate;

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
    private int _parameters = 100;

    @Before
    public void setUp(){
        _agent = mock(PeerAgent.class);
        _modelFactory = mock(ModelFactory.class);
        _model = mock(LogisticModel.class);
        Predicate<Double[]> predicate = x -> x.length == _parameters;

        when(_modelFactory.getLogisticModel(org.mockito.Matchers.<double[]>argThat(new LambdaMatcher(predicate)))).thenReturn(_model);
        _queryable = mock(NoisyQueryable.class);

        when(_agent.getData()).thenReturn(_queryable);

        _creationBehaviour = new ModelCreationBehavior(_agent, _modelFactory, _parameters);
    }

    @Test
    public void action_createsInitialModel(){
        _creationBehaviour.action();

        verify(_model).update(_agent.getUpdateCost(), _queryable);
        verify(_agent).addModel(_model);
    }

}
