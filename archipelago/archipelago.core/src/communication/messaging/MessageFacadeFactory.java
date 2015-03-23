package communication.messaging;

import jade.core.Agent;

/**
 * Created by alex on 3/23/15.
 */
public class MessageFacadeFactory {
    public MessageFacade getFacade(Agent agent) {
        return new MessageFacade(agent);
    }
}
