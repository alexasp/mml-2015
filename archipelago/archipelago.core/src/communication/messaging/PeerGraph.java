package communication.messaging;

import communication.PeerAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by alex on 3/26/15.
 */
public class PeerGraph {
    private static final String SERVICE_NAME = "peer";

    public List<AID> getPeers(Agent agent) {
        DFAgentDescription description = createDescription(agent.getAID());

        try {
            DFAgentDescription[] descriptions = DFService.search(agent, description);
            return IntStream.range(0, descriptions.length)
                    .mapToObj(i -> descriptions[i].getName())
                    .collect(Collectors.toList());

        } catch (FIPAException e) {
            throw new RuntimeException("Unable to search peer graph.", e);
        }
    }

    private DFAgentDescription createDescription(AID aid) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(aid);
        ServiceDescription sd  = new ServiceDescription();
        sd.setName(SERVICE_NAME);
        sd.setType( SERVICE_NAME );
        dfd.addServices(sd);
        return dfd;
    }

    public void join(PeerAgent peerAgent) {
        peerAgent.addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                DFAgentDescription description = createDescription(peerAgent.getAID());
                try {
                    DFService.register(peerAgent, peerAgent.getAID(), description);
                } catch (FIPAException e) {
                    throw new RuntimeException("Unable to register peer agent.", e);
                }
            }
        });
    }

    public AID getMonitoringAgent() {
        throw new UnsupportedOperationException();
    }
}
