package communication.peer;

import communication.BehaviourFactory;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by alex on 4/22/15.
 */
public class GroupLocatorAgent extends Agent {

    public GroupLocatorAgent(BehaviourFactory behaviorFactory){
        addBehaviour(behaviorFactory.getGroupFormingBehaviour());
    }

}
