package communication.peer;

import communication.BehaviourFactory;
import communication.messaging.MessageFacade;
import communication.messaging.MessageFacadeFactory;
import experiment.ExperimentConfiguration;
import jade.core.AID;
import jade.core.Agent;

import java.util.List;

/**
 * Created by alex on 4/22/15.
 */
public class GroupLocatorAgent extends Agent {

    public static final String SERVICE_NAME = "GROUP_LOCATOR";

    public GroupLocatorAgent(List<AID> agents, BehaviourFactory behaviorFactory, ExperimentConfiguration configuration, MessageFacadeFactory messageFacadeFactory){
        addBehaviour(behaviorFactory.getGroupFormingBehaviour(this, agents, configuration, messageFacadeFactory.getFacade(this)));
    }

}
