package communication.messaging;

import communication.PeerAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by alex on 3/26/15.
 */
public class PeerGraph {
    private static final String SERVICE_NAME = "peer";

    public List<AID> getPeers(Agent agent) {
        DFAgentDescription description = createDescription();


        try {
            DFAgentDescription[] descriptions = DFService.search(agent, description);
            return IntStream.range(0, descriptions.length)
                    .mapToObj(i -> descriptions[i].getName())
                    .collect(Collectors.toList());

        } catch (FIPAException e) {
            throw new RuntimeException("Unable to search peer graph.");
        }
    }

    private DFAgentDescription createDescription() {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( SERVICE_NAME );
        dfd.addServices(sd);
        return dfd;
    }

    public void join(PeerAgent peerAgent) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName( peerAgent.getAID() );
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( SERVICE_NAME );
        dfd.addServices(sd);

        try {
            DFService.register(peerAgent, dfd);
        } catch (FIPAException e) {
            throw new RuntimeException("Unable to search peer graph.");
        }
    }
}
