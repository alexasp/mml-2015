package communication.peer;

import com.google.inject.Inject;
import communication.BehaviourFactory;
import communication.PeerAgent;
import communication.messaging.MessageFacadeFactory;
import communication.messaging.PeerGraph;
import experiment.DataLoader;
import experiment.Experiment;
import experiment.ExperimentConfiguration;
import jade.core.AID;
import jade.core.Agent;
import javafx.scene.control.Labeled;
import learning.EnsembleModel;
import learning.IQueryable;
import learning.IQueryableFactory;
import learning.LabeledSample;
import privacy.Budget;
import privacy.NoisyQueryable;
import privacy.NoisyQueryableFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by alex on 3/10/15.
 */
public class AgentFactory {
    private final MessageFacadeFactory _messageFacadeFactory;
    private BehaviourFactory _behaviourFactory;
    private PeerGraph _peerGraph;
    private DataLoader _dataLoader;
    private IQueryableFactory _queryableFactory;

    @Inject
    public AgentFactory(BehaviourFactory behaviourFactory, MessageFacadeFactory messageFacadeFactory, PeerGraph peerGraph, DataLoader dataLoader) {
        _behaviourFactory = behaviourFactory;
        _messageFacadeFactory = messageFacadeFactory;
        _peerGraph = peerGraph;
        _dataLoader = dataLoader;
    }

    public List<PeerAgent> createPeers(List<LabeledSample> data, int parts, int iterations, double budget, int parameters, double updateCost) {
        List<List<LabeledSample>> partitions = _dataLoader.partition(parts, data);

        List<PeerAgent> agents = new ArrayList<>();

        for(List<LabeledSample> partition : partitions){
            agents.add(new PeerAgent(partition, _behaviourFactory, new EnsembleModel(), _messageFacadeFactory, iterations, _peerGraph, parameters, updateCost));
        }

        return agents;
    }

    public CompletionListeningAgent getCompletionAgent(Consumer<Experiment> completionAction, int totalPeerCount, Experiment experiment) {
        return new CompletionListeningAgent(completionAction, totalPeerCount, _behaviourFactory, _messageFacadeFactory, experiment);
    }

    public GroupLocatorAgent getGroupLocatingAgentWithAgents(List<PeerAgent> agents, ExperimentConfiguration configuration) {
        List<AID> aids = agents.stream().map(agent -> agent.getAID()).collect(Collectors.toList());
        return getGroupLocatingAgent(aids, configuration);
    }

    public GroupLocatorAgent getGroupLocatingAgent(List<AID> agents, ExperimentConfiguration configuration) {
        return new GroupLocatorAgent(agents, _behaviourFactory, configuration, _messageFacadeFactory);
    }
}
