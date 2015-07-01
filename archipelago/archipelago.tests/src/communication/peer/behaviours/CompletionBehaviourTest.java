package communication.peer.behaviours;

import communication.PeerAgent;
import communication.messaging.MessageFacade;
import jade.core.AID;
import learning.ParametricModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CompletionBehaviourTest {

    private CompletionBehaviour _behaviour;
    private MessageFacade _messageFacade;
    private String _conversationId = "id";

    @Before
    public void setUp() {
        _messageFacade = mock(MessageFacade.class);

        _behaviour = new CompletionBehaviour(_conversationId, _messageFacade);
    }

    @Test
    public void action_NotifiesCompletionListeningAgent() {

        _behaviour.action();

        verify(_messageFacade).sendCompletionMessage(_conversationId);
    }

}