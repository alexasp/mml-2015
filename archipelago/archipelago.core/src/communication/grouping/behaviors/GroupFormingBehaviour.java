package communication.grouping.behaviors;

import communication.messaging.MessageFacade;
import experiment.ExperimentConfiguration;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import privacy.math.RandomGenerator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by alex on 4/23/15.
 */
public class GroupFormingBehaviour extends CyclicBehaviour {
    private Agent _groupAgent;
    private List<AID> _agents;
    private ExperimentConfiguration _configuration;
    private RandomGenerator _randomGenerator;
    private MessageFacade _messageFacade;
    private int _iteration;

    public GroupFormingBehaviour(Agent groupAgent, List<AID> agents, ExperimentConfiguration configuration, RandomGenerator randomGenerator, MessageFacade messageFacade) {
        _groupAgent = groupAgent;
        _agents = agents;
        _configuration = configuration;
        _randomGenerator = randomGenerator;
        _messageFacade = messageFacade;

        _iteration = 0;
    }

    @Override
    public void action() {
        List<AID> group = IntStream.range(0, _configuration.groupSize)
                .mapToObj(i ->
                                _agents.get(_randomGenerator.uniform(0, _agents.size()-1))
                )
                .collect(Collectors.toList());

        _messageFacade.publishAggregationGroup(group, Integer.toString(_iteration));
//        System.out.println("Formed a new group to aggregate a model.");
        _iteration++;

        if(_iteration == _configuration.iterations) {
            _groupAgent.removeBehaviour(this);
//            _messageFacade.sendCompletionMessage(_groupAgent.getAID());
        }
    }

}
