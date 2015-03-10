package communication.peer;

import communication.BehaviorFactory;
import communication.PeerAgent;
import communication.messaging.MessageFacade;
import communication.peer.behaviours.PeerUpdateBehavior;
import learning.EnsembleModel;
import learning.LabeledExample;
import learning.Model;
import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/5/15.
 */
public class PeerAgentTest {

    private PeerAgent _peerAgent;
    private BehaviorFactory _behaviorFactory;
    private EnsembleModel _ensemble;
    private MessageFacade _messageFacade;
    private NoisyQueryable<LabeledExample> _data;

    @Before
    public void setUp(){
        _data = mock(NoisyQueryable.class);
        _behaviorFactory = mock(BehaviorFactory.class);
        PeerUpdateBehavior peerUpdateBehavior = mock(PeerUpdateBehavior.class);
        when(_behaviorFactory.getPeerUpdate(any(PeerAgent.class), any(MessageFacade.class))).thenReturn(peerUpdateBehavior);

        _ensemble = mock(EnsembleModel.class);
        _messageFacade = mock(MessageFacade.class);

        _peerAgent = new PeerAgent(_data, _behaviorFactory, _ensemble, _messageFacade);
    }

    @Test
    public void contructor_AddsUpdatingBehavior(){
        verify(_behaviorFactory).getPeerUpdate(_peerAgent, _messageFacade);
    }


    @Test
    public void addModel_AddsToEnsemble(){
        Model model = mock(Model.class);

        _peerAgent.addModel(model);

        verify(_ensemble).add(model);
    }

}
