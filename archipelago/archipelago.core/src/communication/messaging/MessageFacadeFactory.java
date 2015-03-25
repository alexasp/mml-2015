package communication.messaging;

import com.google.inject.Inject;
import jade.core.Agent;

/**
 * Created by alex on 3/23/15.
 */
public class MessageFacadeFactory {


    private ACLMessageParser messageParser;

    @Inject
    public MessageFacadeFactory(ACLMessageParser messageParser) {
        this.messageParser = messageParser;
    }

    public MessageFacade getFacade(Agent agent) {
        return new MessageFacade(agent, messageParser);
    }
}
