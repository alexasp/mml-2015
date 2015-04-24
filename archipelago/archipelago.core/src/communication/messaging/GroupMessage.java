package communication.messaging;

import jade.core.AID;

import java.util.List;

/**
 * Created by alex on 4/23/15.
 */
public class GroupMessage {
    public final List<AID> agents;
    public final String conversationId;

    public GroupMessage(List<AID> agents, String conversationId) {
        this.agents = agents;
        this.conversationId = conversationId;
    }
}
