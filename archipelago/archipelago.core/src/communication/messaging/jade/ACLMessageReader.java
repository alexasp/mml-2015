package communication.messaging.jade;


import jade.lang.acl.ACLMessage;

/**
 * Created by alex on 3/23/15.
 */
public class ACLMessageReader {
    public String read(ACLMessage aclMessage) {
        return aclMessage.getContent();
    }
}
