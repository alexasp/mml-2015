package communication.peer;

import com.google.inject.Inject;
import communication.BehaviourFactory;
import communication.PeerAgent;
import communication.messaging.MessageFacadeFactory;
import communication.messaging.PeerGraph;
import experiment.DataLoader;
import experiment.Experiment;
import learning.EnsembleModel;
import learning.LabeledSample;
import privacy.Budget;
import privacy.NoisyQueryable;
import privacy.NoisyQueryableFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by alex on 3/10/15.
 */
public class AgentFactory {
    private final MessageFacadeFactory _messageFacadeFactory;
    private BehaviourFactory _behaviourFactory;
    private PeerGraph _peerGraph;
    private DataLoader _dataLoader;
    private NoisyQueryableFactory _noisyQueryableFactory;

    @Inject
    public AgentFactory(BehaviourFactory behaviourFactory, MessageFacadeFactory messageFacadeFactory, PeerGraph peerGraph, DataLoader dataLoader, NoisyQueryableFactory noisyQueryableFactory) {
        _behaviourFactory = behaviourFactory;
        _messageFacadeFactory = messageFacadeFactory;
        _peerGraph = peerGraph;
        _dataLoader = dataLoader;
        _noisyQueryableFactory = noisyQueryableFactory;
    }

    public List<PeerAgent> createPeers(List<LabeledSample> data, int parts, int iterations, double budget, int parameters, double updateCost) {
        List<List<LabeledSample>> partitions = _dataLoader.partition(parts, data);

        List<PeerAgent> agents = new ArrayList<>();

        for(List<LabeledSample> partition : partitions){
            NoisyQueryable<LabeledSample> queryable = _noisyQueryableFactory.getQueryable(new Budget(budget), partition);
            agents.add(new PeerAgent(queryable, _behaviourFactory, new EnsembleModel(), _messageFacadeFactory, iterations, _peerGraph, parameters, updateCost));
        }

        return agents;
    }

    public CompletionListeningAgent getCompletionAgent(Consumer<Experiment> completionAction, int totalPeerCount, Experiment experiment) {
        return new CompletionListeningAgent(completionAction, totalPeerCount, _behaviourFactory, _messageFacadeFactory, experiment);
    }
}
