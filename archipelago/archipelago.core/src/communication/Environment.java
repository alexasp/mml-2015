package communication;

import com.google.inject.Singleton;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

/**
 * Created by alex on 3/10/15.
 */
@Singleton
public class Environment {

    private AgentContainer _mainContainer;
    private static final String SERVICE_NAME = "peer";
    private int _counter = 0;


    public Environment() throws ControllerException {
        createContainer();
    }

    /**
     * Based on code by James Malone here: http://jade.tilab.com/pipermail/jade-develop/2008q3/012874.html
     */
    private void createContainer() throws ControllerException {
        // Get a hold on JADE runtime
        Runtime rt = Runtime.instance();

       // Exit the JVM when there are no more containers around
        rt.setCloseVM(true);
        System.out.print("runtime created\n");

        // Create a default profile
        Profile profile = new ProfileImpl(null, 1201, null);
        System.out.print("profile created\n");

        System.out.println("Launching a whole in-process platform..."+profile);
        _mainContainer = rt.createMainContainer(profile);

       // now set the default Profile to start a container
        ProfileImpl pContainer = new ProfileImpl(null, 1202, null);
        System.out.println("Launching the agent container ..."+pContainer);

//        jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);
//        System.out.println("Launching the agent container after ..."+pContainer);

        System.out.println("containers created");
        System.out.println("Launching the rma agent on the main container ...");

        AgentController rma = _mainContainer.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
        rma.start();
//        _mainContainer.suspend();
    }

//    public static void main(String[] args) throws ControllerException {
//        Environment env = new Environment();
//        env.createContainer();
//    }

    public void registerAgent(Agent agent) throws StaleProxyException {
        AgentController agentWrapper = _mainContainer.acceptNewAgent("peerolini" + _counter, agent);
        _counter++;
        agentWrapper.start();
//        agentWrapper.suspend();
//        joinDF(agent);
    }



    public void run() throws ControllerException {
        _mainContainer.start();
    }

    public void stop() throws ControllerException {
        _mainContainer.suspend();
    }
}
