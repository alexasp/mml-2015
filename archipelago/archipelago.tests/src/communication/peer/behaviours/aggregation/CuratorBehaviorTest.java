package communication.peer.behaviours.aggregation;

import communication.PeerAgent;
import communication.messaging.Message;
import communication.messaging.MessageFacade;
import communication.peer.ArchipelagoPerformatives;
import experiment.ExperimentConfiguration;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import learning.ModelMerger;
import learning.ParametricModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import privacy.math.RandomGenerator;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CuratorBehaviorTest {


    private CuratorBehavior _curatorBehavior;
    private MessageFacade _messageFacade;
    private Message _message1;
    private Message _message2;
    private AID _agent1;
    private AID _agent2;
    private AID _agent3;
    private List<AID> _parties;
    private ModelMerger _modelMerger;
    private ParametricModel _model1;
    private ParametricModel _model2;
    private ParametricModel _model3;
    private PeerAgent _peerAgent;
    private double[] _noise = new double[]{-0.33, 1.55, 2.50};
    private RandomGenerator _randomGenerator;
    private int _dataLength1 = 17;
    private int _dataLength2 = 30;
    private ExperimentConfiguration _configuration;
    private String _conversationId = "id";

    @Before
    public void setUp(){
        _peerAgent = mock(PeerAgent.class);
        when(_peerAgent.getEpsilon()).thenReturn(0.1);
        _configuration = mock(ExperimentConfiguration.class);
        _configuration.regularization = 1.0;
        _messageFacade = mock(MessageFacade.class);
        setUpMessages();
        mockAgents();
        _parties = Arrays.asList(_agent1, _agent2);
        _randomGenerator = mock(RandomGenerator.class);
        double beta = 2.0/(_peerAgent.getEpsilon()*_dataLength1*_configuration.regularization);
        when(_randomGenerator.fromLaplacian(AdditionalMatchers.eq(beta, 0.0001d), eq(3)))
                .thenReturn(_noise);

        _curatorBehavior = new CuratorBehavior(_parties, _conversationId, _peerAgent, _messageFacade, _modelMerger, _randomGenerator, _configuration);
    }

    private void setUpModels() {
        _modelMerger = mock(ModelMerger.class);
        _model1 = mock(ParametricModel.class);
        _model2 = mock(ParametricModel.class);
        _model3 = mock(ParametricModel.class);
        when(_model1.getParameters()).thenReturn(new double[]{2.0, 1.0, -1.0});
        when(_model2.getParameters()).thenReturn(new double[]{1.0, 2.0, 4.0});

        when(_modelMerger.merge(anyListOf(ParametricModel.class))).thenReturn(_model3);
    }

    private void mockAgents() {
        _agent1 = mock(AID.class);
        _agent2 = mock(AID.class);
    }

    private void setUpMessages() {
        setUpModels();

        when(_messageFacade.hasMessage(ArchipelagoPerformatives.ModelContribution, _conversationId)).thenReturn(true);
        _message1 = mock(Message.class);
        when(_message1.getModel()).thenReturn(_model1);
        when(_message1.getDatasetSize()).thenReturn(_dataLength1);
        _message2 = mock(Message.class);
        when(_message2.getModel()).thenReturn(_model2);
        when(_message2.getDatasetSize()).thenReturn(_dataLength2);
    }

    @Test
    public void isCyclic() {
        assertTrue(_curatorBehavior instanceof CyclicBehaviour);
    }

    @Test
    public void action_AllParticipantsResponded_PublishesNoisyAveragedModel() {
        when(_messageFacade.nextMessage(ArchipelagoPerformatives.ModelContribution, _conversationId)).thenReturn(_message1);
        _curatorBehavior.action();
        when(_messageFacade.nextMessage(ArchipelagoPerformatives.ModelContribution, _conversationId)).thenReturn(_message2);
        _curatorBehavior.action();

        verify(_model3).addTerm(_noise);
        verify(_messageFacade).sendToPeer(_agent1, _model3, ArchipelagoPerformatives.AggregatedResult, _conversationId);
        verify(_messageFacade).sendToPeer(_agent2, _model3, ArchipelagoPerformatives.AggregatedResult, _conversationId);
    }


    @Test
    public void aciton_AllParticipantsResponded_RemovesBehavior(){
        when(_messageFacade.nextMessage(ArchipelagoPerformatives.ModelContribution, _conversationId)).thenReturn(_message1);

        _curatorBehavior.action();
        verify(_peerAgent, never()).removeBehaviour(any(Behaviour.class));

        _curatorBehavior.action();
        verify(_peerAgent).removeBehaviour(_curatorBehavior);
    }

    @Test
    public void action_AllParticipantsResponded_SendsCompletionMessage(){
        when(_messageFacade.nextMessage(ArchipelagoPerformatives.ModelContribution, _conversationId)).thenReturn(_message1);

        _curatorBehavior.action();
        verify(_messageFacade, never()).sendCompletionMessage(anyString());

        _curatorBehavior.action();
        verify(_messageFacade).sendCompletionMessage(_conversationId);
    }

}