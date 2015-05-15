package experiment;

import communication.Environment;
import communication.PeerAgent;
import communication.messaging.PeerGraph;
import communication.peer.AgentFactory;
import communication.peer.CompletionListeningAgent;
import communication.peer.GroupLocatorAgent;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import learning.LabeledSample;
import learning.metrics.ConfusionMatrix;
import learning.metrics.PerformanceMetrics;
import learning.metrics.ROC_Curve;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by alex on 3/17/15.
 */
public class Experiment {
    private final List<PeerAgent> _peers;
    private final PerformanceMetrics _performanceMetrics;
    private final List<LabeledSample> _trainData;
    private final List<LabeledSample> _testData;
    private final AgentFactory _agentFactory;
    private PeerGraph _peerGraph;
    private final ExperimentConfiguration _configuration;


    private Environment _environment;
    private static CompletionListeningAgent _completionAgent;
    private GroupLocatorAgent _groupAgent;


    public Experiment(List<LabeledSample> trainData,List<LabeledSample> testData, AgentFactory agentFactory, PerformanceMetrics performanceMetrics, Environment environment, DataLoader dataLoader, PeerGraph peerGraph, ExperimentConfiguration configuration) throws ControllerException {
        _environment = environment;
        _agentFactory = agentFactory;
        _peerGraph = peerGraph;

        _configuration = configuration;

        _trainData = trainData;
        _testData = testData;

        _performanceMetrics = performanceMetrics;
        _peers = agentFactory.createPeers(_trainData, configuration);

        registerPeers(_peers);
    }

    private void registerPeers(List<PeerAgent> peers) throws StaleProxyException {
        for(PeerAgent peer : peers){
            _environment.registerAgent(peer);
            _peerGraph.join(peer, PeerAgent.SERVICE_NAME);
        }
    }

    public void run(Consumer<Experiment> actionOnCompletion) throws ControllerException, InterruptedException {
        Consumer<Experiment> completionAction =
                experiment ->
        {
            _environment.reset();
            deregisterAgents();
            actionOnCompletion.accept(experiment);
        };

        if(_completionAgent == null) {
            _completionAgent = _agentFactory.getCompletionAgent();
            _environment.registerAgent(_completionAgent, "CompletionAgent");
            _peerGraph.join(_completionAgent, CompletionListeningAgent.SERVICE_NAME);
        }

        _completionAgent.init(completionAction, _peers.size(), this, _configuration.aggregations);
        _groupAgent = _agentFactory.getGroupLocatingAgentWithAgents(_peers, _configuration);

        _peerGraph.getRegistrationLatch().await();


        _environment.registerAgent(_groupAgent, "GroupingAgent");

    }

    private void deregisterAgents() {
//        _peerGraph.deregister(_completionAgent, CompletionListeningAgent.SERVICE_NAME);
//        _completionAgent.doDelete();
        _groupAgent.doDelete();

        for (PeerAgent peer : _peers) {
            _peerGraph.deregister(peer, PeerAgent.SERVICE_NAME);
            peer.doDelete();
        }

    }

    public List<Double> test() {
        return _peers.stream()
                .mapToDouble(peer -> _performanceMetrics.errorRate(_testData, peer.labelData(_testData)))
                .boxed()
                .collect(Collectors.toList());
    }

    public ROC_Curve writeRocCurves(String path) {

        ROC_Curve roc = new ROC_Curve(path);

        for (PeerAgent peer : _peers) {

            List<ConfusionMatrix> confusionMatrices = new ArrayList<>();

            for (double threshold = 1.0d; threshold >= 0.0d; threshold = threshold - 0.05d) {

                ConfusionMatrix matrix = new ConfusionMatrix(_testData, peer.labelData(_testData, threshold), threshold);
                confusionMatrices.add(matrix);

            }
            roc.add(confusionMatrices, peer.getLocalName());
        }

        roc.writeChartToFile();

        return roc;
    }


    public List<LabeledSample> getData() {
        return _trainData;
    }

    public ExperimentConfiguration getConfiguration() {
        return _configuration;
    }

}
