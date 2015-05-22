package communication;

import com.google.inject.Inject;
import communication.grouping.behaviors.GroupFormingBehaviour;
import communication.messaging.MessageFacade;
import communication.peer.CompletionListeningAgent;
import communication.peer.behaviours.*;
import communication.peer.behaviours.aggregation.ContributorBehavior;
import communication.peer.behaviours.aggregation.CuratorBehavior;
import experiment.ExperimentConfiguration;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import learning.ModelMerger;
import learning.ParametricModel;
import learning.ModelFactory;
import privacy.math.RandomGenerator;

import java.util.List;

/**
 * Created by alex on 3/9/15.
 */
public class BehaviourFactory {
    private ModelFactory _modelFactory;
    private ModelMerger _modelMerger;
    private RandomGenerator _randomGenerator;
    private ExperimentConfiguration _configuration;

    @Inject
    public BehaviourFactory(ModelFactory logisticModelFactory, ModelMerger modelMerger, RandomGenerator randomGenerator, ExperimentConfiguration configuration) {
        _modelFactory = logisticModelFactory;
        _modelMerger = modelMerger;
        _randomGenerator = randomGenerator;
        _configuration = configuration;
    }

    public Behaviour getPeerUpdate(PeerAgent peerAgent, MessageFacade messageFacade) {
        return new PeerUpdateBehavior(peerAgent, messageFacade);
    }

    public Behaviour getModelPropegate(PeerAgent agent, ParametricModel model) {
        return new PropegateBehavior(model, agent);
    }

    public Behaviour getModelCreation(PeerAgent agent, int parameters) {
        return new ModelCreationBehavior(agent, _modelFactory, this, parameters);
    }

    public Behaviour getCompletionListening(CompletionListeningAgent agent, MessageFacade messageFacade) {
        return new CompletionListeningBehavior(agent, messageFacade);
    }

    public Behaviour getCompletionBehavior(String conversationId, MessageFacade facade) {
        return new CompletionBehaviour(conversationId, facade);
    }

    public ModelAggregationBehavior getModelAggregation(PeerAgent agent, MessageFacade messageFacade) {
        return new ModelAggregationBehavior(agent, messageFacade, this);
    }

    public Behaviour getContributorBehavior(PeerAgent agent, AID curator, MessageFacade messageFacade, String conversationId) {
        return new ContributorBehavior(agent, curator, conversationId, messageFacade);
    }

    public Behaviour getCuratorBehavior(List<AID> parties, MessageFacade messageFacade, PeerAgent peerAgent, String conversationId) {
        return new CuratorBehavior(parties, conversationId, peerAgent, messageFacade, _modelMerger, _randomGenerator, _configuration, this);
    }

    public Behaviour getGroupFormingBehaviour(Agent groupAgent, List<AID> _agents, ExperimentConfiguration configuration, MessageFacade messageFacade) {
        return new GroupFormingBehaviour(groupAgent, _agents, configuration, _randomGenerator, messageFacade);
    }
}
