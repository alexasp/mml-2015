package communication.peer.behaviours;

import communication.messaging.Message;
import communication.messaging.MessageFacade;
import communication.peer.CompletionListeningAgent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by aspis on 26.03.2015.
 */
public class CompletionListeningBehaviorTest {

    private CompletionListeningBehavior _behavior;
    private MessageFacade _messageFacade;
    private CompletionListeningAgent _agent;

    @Before
    public void setUp(){
        _messageFacade = mock(MessageFacade.class);
        _agent = mock(CompletionListeningAgent.class);

        when(_messageFacade.hasMessage(CompletionListeningBehavior.Performative)).thenReturn(true);
        _behavior = new CompletionListeningBehavior(_agent, _messageFacade);
    }

    @Test
    public void action_NewMessage_NotifiesAgentAboutCompletion() throws Exception {
        when(_messageFacade.nextMessage(CompletionListeningBehavior.Performative)).thenReturn(new Message());
        
        _behavior.action();
        
        verify(_agent).anAgentCompleted();
    }

    @Test
    public void action_NoMessage_NotifiesAgentAboutCompletion() throws Exception {
        when(_messageFacade.hasMessage(CompletionListeningBehavior.Performative)).thenReturn(false);

        _behavior.action();

        verify(_messageFacade, never()).nextMessage(CompletionListeningBehavior.Performative);
        verify(_agent, never()).anAgentCompleted();
    }
}