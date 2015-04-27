package communication.peer;

import communication.BehaviourFactory;
import communication.messaging.MessageFacade;
import experiment.ExperimentConfiguration;
import jade.core.AID;
import jade.core.Agent;

import java.util.List;

/**
 * Created by alex on 4/22/15.
 */
public class GroupLocatorAgent extends Agent {

    public GroupLocatorAgent(List<AID> agents, BehaviourFactory behaviorFactory, ExperimentConfiguration configuration, MessageFacade messageFacade){
        addBehaviour(behaviorFactory.getGroupFormingBehaviour(this, agents, configuration, messageFacade));
    }

}
