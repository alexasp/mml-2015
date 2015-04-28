package communication.peer;

import communication.BehaviourFactory;
import communication.messaging.MessageFacade;
import communication.messaging.MessageFacadeFactory;
import communication.peer.behaviours.CompletionListeningBehavior;
import experiment.Experiment;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by aspis on 25.03.2015.
 */
public class CompletionListeningAgentTest {

    private Consumer<Experiment> _completionAction;
    private CompletionListeningAgent _completionListeningAgent;
    private BehaviourFactory _behaviourFactory;
    private int _totalPeerCount = 17;
    private Experiment _experiment;
    private MessageFacadeFactory _messageFacadeFactory;
    private MessageFacade _messageFacade;
    private jade.core.behaviours.Behaviour _completionListeningBehavior;
    private int _iterations = 20;

    @Before
    public void setUp() {
        _behaviourFactory = mock(BehaviourFactory.class);
        _completionAction = mock(Consumer.class);
        _experiment = mock(Experiment.class);
        _messageFacadeFactory = mock(MessageFacadeFactory.class);
        _messageFacade = mock(MessageFacade.class);
        when(_messageFacadeFactory.getFacade(any(CompletionListeningAgent.class))).thenReturn(_messageFacade);
        _completionListeningBehavior = mock(CompletionListeningBehavior.class);
        when(_behaviourFactory.getCompletionListening(any(CompletionListeningAgent.class), same(_messageFacade))).thenReturn(_completionListeningBehavior);


        _completionListeningAgent = new CompletionListeningAgent(_completionAction, _totalPeerCount, _behaviourFactory, _messageFacadeFactory, _experiment, _iterations);
    }

    @Test
    public void addsListeningBehaviour(){
        verify(_behaviourFactory).getCompletionListening(_completionListeningAgent, _messageFacade);
    }

    @Test
    public void allIterationsCompleted_RunsAction(){

        IntStream.iterate(0, i -> i+1).limit(_iterations-1).forEach(i -> _completionListeningAgent.anAgentCompleted());
        verify(_completionAction, never()).accept(any(Experiment.class));
        _completionListeningAgent.anAgentCompleted();

        verify(_completionAction).accept(any(Experiment.class));
    }
}
