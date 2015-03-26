package communication.peer;

import com.google.inject.Inject;
import communication.BehaviorFactory;
import communication.PeerAgent;
import communication.messaging.MessageFacadeFactory;
import experiment.CompletionListeningAgent;
import experiment.Experiment;
import learning.EnsembleModel;
import learning.LabeledSample;
import privacy.NoisyQueryable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by alex on 3/10/15.
 */
public class AgentFactory {
    private final MessageFacadeFactory _messageFacadeFactory;
    private BehaviorFactory _behaviorFactory;

    @Inject
    public AgentFactory(BehaviorFactory behaviorFactory, MessageFacadeFactory messageFacadeFactory) {
        _behaviorFactory = behaviorFactory;
        _messageFacadeFactory = messageFacadeFactory;
    }

    public List<PeerAgent> createPeers(NoisyQueryable<LabeledSample> data, int parts, int iterations) {
        List<NoisyQueryable<LabeledSample>> partitions = data.partition(parts);

        List<PeerAgent> agents = new ArrayList<>();

        for(NoisyQueryable<LabeledSample> partition : partitions){
            agents.add(new PeerAgent(partition, _behaviorFactory, new EnsembleModel(), _messageFacadeFactory, iterations));
        }

        return agents;
    }

    public CompletionListeningAgent getCompletionAgent(Consumer<Experiment> completionAction) {
        return new CompletionListeningAgent(completionAction);
    }
}
