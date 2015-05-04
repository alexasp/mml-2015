package communication.peer;

import communication.BehaviourFactory;
import communication.messaging.PeerGraph;
import communication.peer.behaviours.ModelAggregationBehavior;
import communication.peer.behaviours.ModelCreationBehavior;
import communication.PeerAgent;
import communication.messaging.MessageFacade;
import communication.messaging.MessageFacadeFactory;
import communication.peer.behaviours.PeerUpdateBehavior;
import experiment.ExperimentConfiguration;
import jade.core.Agent;
import learning.EnsembleModel;
import learning.ParametricModel;
import org.apache.commons.math3.analysis.function.Exp;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 3/5/15.
 */
public class PeerAgentTest {

    private PeerAgent _peerAgent;
    private BehaviourFactory _behaviourFactory;
    private EnsembleModel _ensemble;
    private MessageFacadeFactory _messageFacadeFactory;
    private int _iterations = 5;
    private PeerGraph _peerGraph;
    private int _parameters = 60;
    private double _updateCost = 0.1;
    private List _data;

    @Before
    public void setUp(){
        _data = mock(List.class);
        _ensemble = mock(EnsembleModel.class);

        _messageFacadeFactory = mock(MessageFacadeFactory.class);
        _behaviourFactory = mock(BehaviourFactory.class);
        _peerGraph = mock(PeerGraph.class);

        stubBehaviourFactory(_behaviourFactory, _parameters);
        stubMessageFacadeFactory(_messageFacadeFactory);

        _peerAgent = new PeerAgent(_data, _behaviourFactory, _ensemble, _messageFacadeFactory, _iterations, _peerGraph, _parameters, _updateCost);
    }



    public static void stubMessageFacadeFactory(MessageFacadeFactory messageFacadeFactory) {
        MessageFacade messageFacade = mock(MessageFacade.class);
        when(messageFacadeFactory.getFacade(any(PeerAgent.class))).thenReturn(messageFacade);
    }

    public static void stubBehaviourFactory(BehaviourFactory behaviourFactory, int parameters) {
        when(behaviourFactory.getPeerUpdate(any(PeerAgent.class), any(MessageFacade.class))).thenReturn(mock(PeerUpdateBehavior.class));
        when(behaviourFactory.getModelCreation(any(PeerAgent.class), eq(parameters))).thenReturn(mock(ModelCreationBehavior.class));
        when(behaviourFactory.getModelAggregation(any(PeerAgent.class), any(MessageFacade.class))).thenReturn(mock(ModelAggregationBehavior.class));
    }

    @Test
    public void construction_GetsBehaviors() {
        verify(_behaviourFactory).getModelAggregation(same(_peerAgent), any(MessageFacade.class));
    }



    @Test
    public void constructor_SchedulesInitialModelTraining(){
        verify(_behaviourFactory).getModelCreation(_peerAgent, _parameters);
    }


    @Test
    public void addModel_AddsToEnsemble(){
        ParametricModel model = mock(ParametricModel.class);

        _peerAgent.addModel(model);

        verify(_ensemble).add(model);
    }

}
