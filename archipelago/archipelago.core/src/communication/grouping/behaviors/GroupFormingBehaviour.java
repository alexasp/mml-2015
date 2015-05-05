package communication.grouping.behaviors;

import communication.messaging.MessageFacade;
import experiment.ExperimentConfiguration;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import privacy.math.RandomGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alex on 4/23/15.
 */
public class GroupFormingBehaviour extends CyclicBehaviour {
    private final HashMap<AID, Double> _budgets;
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

        _budgets = setUpBudgets(_agents);

        _iteration = 0;
    }

    private HashMap<AID, Double> setUpBudgets(List<AID> _agents) {
        HashMap<AID, Double> map = new HashMap<>();
        for (AID agent : _agents) {
            map.put(agent, _configuration.epsilon);
        }
        return map;
    }

    @Override
    public void action() {

        List<AID> group = _randomGenerator.sample(_agents, _configuration.groupSize);

        _messageFacade.publishAggregationGroup(group, Integer.toString(_iteration));
        _iteration++;

        updateAgentBudgetsAndAvailability(group);

        if(_agents.size() < _configuration.groupSize){
            _groupAgent.removeBehaviour(this);
        }

        if(_iteration == _configuration.aggregations) {
            _groupAgent.removeBehaviour(this);
        }
    }

    private void updateAgentBudgetsAndAvailability(List<AID> group) {
        for (AID aid : group) {
            _budgets.put(aid, _budgets.get(aid)-_configuration.budget);
            if(_budgets.get(aid) < _configuration.budget - 0.0000001d) {
                _agents.remove(aid);
            }
        }
    }

}
