package communication.peer;

import communication.BehaviorFactory;
import communication.ModelCreationBehavior;
import communication.PeerAgent;
import communication.messaging.Message;
import communication.messaging.MessageFacade;
import communication.messaging.MessageFacadeFactory;
import communication.peer.behaviours.PeerUpdateBehavior;
import learning.EnsembleModel;
import learning.LabeledSample;
import learning.Model;
import org.junit.Before;
import org.junit.Test;
import privacy.NoisyQueryable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
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
    private NoisyQueryable<LabeledSample> _data;
    private MessageFacadeFactory _messageFacadeFactory;

    @Before
    public void setUp(){
        _data = mock(NoisyQueryable.class);
        _ensemble = mock(EnsembleModel.class);

        _messageFacadeFactory = mock(MessageFacadeFactory.class);
        _behaviorFactory = mock(BehaviorFactory.class);

        stubBehaviourFactory(_behaviorFactory);
        stubMessageFacadeFactory(_messageFacadeFactory);

        _peerAgent = new PeerAgent(_data, _behaviorFactory, _ensemble, _messageFacadeFactory);
    }

    public static void stubMessageFacadeFactory(MessageFacadeFactory messageFacadeFactory) {
        MessageFacade messageFacade = mock(MessageFacade.class);
        when(messageFacadeFactory.getFacade(any(PeerAgent.class))).thenReturn(messageFacade);
    }

    public static void stubBehaviourFactory(BehaviorFactory behaviorFactory) {
        when(behaviorFactory.getPeerUpdate(any(PeerAgent.class), any(MessageFacade.class))).thenReturn(mock(PeerUpdateBehavior.class));
        when(behaviorFactory.getModelCreation(any(PeerAgent.class))).thenReturn(mock(ModelCreationBehavior.class));
    }

    @Test
    public void contructor_AddsUpdatingBehavior(){
        verify(_behaviorFactory).getPeerUpdate(same(_peerAgent), any(MessageFacade.class));
    }

    @Test
    public void constructor_SchedulesInitialModelTraining(){
        verify(_behaviorFactory).getModelCreation(_peerAgent);
    }


    @Test
    public void addModel_AddsToEnsemble(){
        Model model = mock(Model.class);

        _peerAgent.addModel(model);

        verify(_ensemble).add(model);
    }


}
