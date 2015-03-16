package communication.peer.behaviours;

import communication.PeerAgent;
import communication.messaging.MessageFacade;
import learning.LabeledExample;
import learning.Model;
import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/16/15.
 */
public class PropegateBehaviorTest {


    private MessageFacade _messaging;
    private PropegateBehavior _propegateBehavior;
    private Model _model;
    private PeerAgent _peerAgent;
    private NoisyQueryable<LabeledExample> _queryable;
    private double _epsilon = 1.0;

    @Before
    public void setUp(){
        _model = mock(Model.class);
        _queryable = mock(NoisyQueryable.class);
        _peerAgent = mock(PeerAgent.class);
        _messaging = mock(MessageFacade.class);
        when(_peerAgent.getMessageFacade()).thenReturn(_messaging);
        when(_peerAgent.getUpdateCost()).thenReturn(_epsilon);

        when(_peerAgent.getData()).thenReturn(_queryable);

        _propegateBehavior = new PropegateBehavior(_model, _peerAgent);
    }

    @Test
    public void action_UpdatesModel(){
        _propegateBehavior.action();

        verify(_model).update(_epsilon, _queryable);
    }


    @Test
    public void action_sendsModelToRandomPeer(){
        _propegateBehavior.action();

        verify(_messaging).sendToRandomPeer(_model);
    }

}
