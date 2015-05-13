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
import learning.EnsembleModel;
import learning.LabeledSample;

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

    @Inject
    public AgentFactory(BehaviourFactory behaviourFactory, MessageFacadeFactory messageFacadeFactory, PeerGraph peerGraph, DataLoader dataLoader) {
        _behaviourFactory = behaviourFactory;
        _messageFacadeFactory = messageFacadeFactory;
        _peerGraph = peerGraph;
        _dataLoader = dataLoader;
    }

    public List<PeerAgent> createPeers(List<LabeledSample> data, ExperimentConfiguration configuration) {
        List<List<LabeledSample>> partitions = _dataLoader.partition(configuration.peerCount, data, configuration.recordsPerPeer);

        List<PeerAgent> agents = new ArrayList<>();

        for(List<LabeledSample> partition : partitions){
            agents.add(new PeerAgent(partition, _behaviourFactory, new EnsembleModel(), _messageFacadeFactory,
                    configuration.aggregations, _peerGraph, configuration.parameters, configuration.updateCost));
        }

        return agents;
    }

    public CompletionListeningAgent getCompletionAgent() {
        return new CompletionListeningAgent(_messageFacadeFactory, _behaviourFactory);
    }

    public GroupLocatorAgent getGroupLocatingAgentWithAgents(List<PeerAgent> agents, ExperimentConfiguration configuration) {
        List<AID> aids = agents.stream().map(agent -> agent.getAID()).collect(Collectors.toList());
        return getGroupLocatingAgent(aids, configuration);
    }

    public GroupLocatorAgent getGroupLocatingAgent(List<AID> agents, ExperimentConfiguration configuration) {
        return new GroupLocatorAgent(agents, _behaviourFactory, configuration, _messageFacadeFactory);
    }
}
