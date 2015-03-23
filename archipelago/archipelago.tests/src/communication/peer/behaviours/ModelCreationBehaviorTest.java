package communication.peer.behaviours;

import communication.PeerAgent;
import learning.LabeledSample;
import learning.ModelFactory;
import learning.models.LogisticModel;
import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;

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

    @Before
    public void setUp(){
        _agent = mock(PeerAgent.class);
        _modelFactory = mock(ModelFactory.class);
        _model = mock(LogisticModel.class);
        when(_modelFactory.getLogisticModel()).thenReturn(_model);
        _queryable = mock(NoisyQueryable.class);

        when(_agent.getData()).thenReturn(_queryable);

        _creationBehaviour = new ModelCreationBehavior(_agent, _modelFactory);
    }

    @Test
    public void action_createsInitialModel(){
        _creationBehaviour.action();

        verify(_model).update(_agent.getUpdateCost(), _queryable);
        verify(_agent).addModel(_model);
    }

}
