package communication;

import com.google.inject.Singleton;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.Logger;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.util.logging.Level;

/**
 * Created by alex on 3/10/15.
 */
@Singleton
public class Environment {

    private AgentContainer _mainContainer;
    private static final String SERVICE_NAME = "peer";
    private int _counter = 0;
    private Runtime _rt;


    public Environment() throws ControllerException {
        createContainer();
    }

    /**
     * Based on code by James Malone here: http://jade.tilab.com/pipermail/jade-develop/2008q3/012874.html
     */
    private void createContainer() throws ControllerException {
        // Get a hold on JADE runtime
        Logger.getJADELogger(this.getClass().getName()).setLevel(Level.OFF);
        _rt = Runtime.instance();
       // Don't exit the JVM when there are no more containers around
        _rt.setCloseVM(false);


        // Create a default profile
        Profile profile = new ProfileImpl(null, 1201, null);

        _mainContainer = _rt.createMainContainer(profile);

       // now set the default Profile to start a container
        ProfileImpl pContainer = new ProfileImpl(null, 1202, null);

//        jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);
//        System.out.println("Launching the agent container after ..."+pContainer);


//        AgentController rma = _mainContainer.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
//        rma.start();
//        _mainContainer.suspend();
    }

//    public static void main(String[] args) throws ControllerException {
//        Environment env = new Environment();
//        env.createContainer();
//    }

    public void registerAgent(Agent agent) throws StaleProxyException {
        registerAgent(agent, "peerolini" + _counter);
        _counter++;
    }

    public void registerAgent(Agent agent, String agentName) throws StaleProxyException {
        AgentController agentWrapper = _mainContainer.acceptNewAgent(agentName, agent);
        agentWrapper.start();
    }



    public void run() throws ControllerException {
        _mainContainer.start();
    }

    public void stop() throws ControllerException {
        _mainContainer.suspend();
    }

    public void clearAndKill() {
        try {
//            _mainContainer.kill();
            _mainContainer.getPlatformController().kill();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        _rt.shutDown();
    }
}
