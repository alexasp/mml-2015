package communication.peer.behaviours;

import communication.PeerAgent;
import communication.messaging.MessageFacade;
import learning.LabeledSample;
import learning.ParametricModel;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/16/15.
 */
public class PropegateBehaviorTest {


    private MessageFacade _messaging;
    private PropegateBehavior _propegateBehavior;
    private ParametricModel _model;
    private PeerAgent _peerAgent;
    private List<LabeledSample> _data;
    private double _epsilon = 1.0;

    @Before
    public void setUp(){
        _model = mock(ParametricModel.class);
        _data = mock(List.class);
        _peerAgent = mock(PeerAgent.class);
        _messaging = mock(MessageFacade.class);
        when(_peerAgent.getMessageFacade()).thenReturn(_messaging);
        when(_peerAgent.getEpsilon()).thenReturn(_epsilon);

        when(_peerAgent.getData()).thenReturn(_data);

        _propegateBehavior = new PropegateBehavior(_model, _peerAgent);
    }

    @Test
    public void action_UpdatesModel(){
        _propegateBehavior.action();

        verify(_model).update(_epsilon, _data);
    }


    @Test
    public void action_sendsModelToRandomPeer(){
        _propegateBehavior.action();

        verify(_messaging).sendToRandomPeer(_model);
    }

}
