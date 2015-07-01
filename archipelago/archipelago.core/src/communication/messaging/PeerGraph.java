package communication.messaging;

import com.google.inject.Inject;
import communication.PeerAgent;
import communication.peer.CompletionListeningAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import org.apache.poi.ss.formula.functions.Count;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by alex on 3/26/15.
 */
public class PeerGraph {
    private static final String PEER_SERVICE = "peer";
    private static final String COMPLETION_SERVICE = "completion";
    private final CountDownLatch RegistrationLatch;

    @Inject
    public PeerGraph(CountDownLatch registrationLatch) {
        RegistrationLatch = registrationLatch;
    }


    public List<AID> getPeers(Agent agent) {
        DFAgentDescription description = createDescription(PEER_SERVICE);

        try {
            DFAgentDescription[] descriptions = DFService.search(agent, description);
            return IntStream.range(0, descriptions.length)
                    .mapToObj(i -> descriptions[i].getName())
                    .collect(Collectors.toList());

        } catch (FIPAException e) {
            throw new RuntimeException("Unable to search peer graph.", e);
        }
    }

    public AID getMonitoringAgent(Agent agent) {
        DFAgentDescription description = createDescription(COMPLETION_SERVICE);

        try {
            DFAgentDescription[] descriptions = DFService.search(agent, description);
            return IntStream.range(0, descriptions.length)
                    .mapToObj(i -> descriptions[i].getName())
                    .findFirst().get();

        } catch (FIPAException e) {
            throw new RuntimeException("Unable to search peer graph.", e);
        }
    }

    private DFAgentDescription createDescription(AID aid, String serviceName) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(aid);
        ServiceDescription sd  = new ServiceDescription();
        sd.setName(serviceName);
        sd.setType( serviceName );
        dfd.addServices(sd);
        return dfd;
    }

    private DFAgentDescription createDescription(String serviceName) {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( serviceName );
        dfd.addServices(sd);
        return dfd;
    }



    public void join(Agent agent, String serviceName) {
        agent.addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                DFAgentDescription description = createDescription(agent.getAID(), serviceName);
                try {
                    DFService.register(agent, description);
//                    System.out.println("Registered peer.");
                    RegistrationLatch.countDown();
                } catch (FIPAException e) {
                    throw new RuntimeException("Unable to register agent.", e);
                }
            }
        });
    }

    public CountDownLatch getRegistrationLatch() {
        return RegistrationLatch;
    }

    public void deregister(Agent completionAgent, String serviceName) {
        try {
            DFService.deregister(completionAgent);
        } catch (FIPAException e) {
            throw new RuntimeException("Unable to deregister agent.", e);
        }
    }
}
