package communication.messaging;

import jade.core.Agent;

/**
 * Created by alex on 3/23/15.
 */
public class MessageFacadeFactory {


    private MessageParser messageParser;

    public MessageFacadeFactory(MessageParser messageParser) {
        this.messageParser = messageParser;
    }

    public MessageFacade getFacade(Agent agent) {
        return new MessageFacade(agent, messageParser);
    }
}
