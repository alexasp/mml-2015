package communication;

import communication.PeerAgent;
import communication.peer.behaviours.PeerUpdateBehavior;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/5/15.
 */
public class PeerAgentTest {

    private PeerAgent _peerAgent;
    private BehaviorFactory _behaviorFactory;

    @Before
    public void setUp(){
        _behaviorFactory = mock(BehaviorFactory.class);

        _peerAgent = new PeerAgent(_behaviorFactory);
    }

    @Test
    public void contructor_AddsUpdatingBehavior(){
        PeerUpdateBehavior peerUpdateBehavior = mock(PeerUpdateBehavior.class);
        when(_behaviorFactory.getPeerUpdate()).thenReturn(peerUpdateBehavior);

        verify(_behaviorFactory.getPeerUpdate());
    }


}
