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
    private List<LabeledSample> _testData;
    private final List<LabeledSample> _data;
    private final AgentFactory _agentFactory;
    private PeerGraph _peerGraph;
    private final ExperimentConfiguration _configuration;


    private Environment _environment;


    public Experiment(List<LabeledSample> samples, AgentFactory agentFactory, PerformanceMetrics performanceMetrics, Environment environment, DataLoader dataLoader, PeerGraph peerGraph, ExperimentConfiguration configuration) throws StaleProxyException {
        _environment = environment;
        _agentFactory = agentFactory;
        _peerGraph = peerGraph;

        _configuration = configuration;

        List<List<LabeledSample>> trainPartitioning = dataLoader.partition(configuration.trainRatio, samples);
        _data = samples;
        _trainData = trainPartitioning.get(0);
        _testData = trainPartitioning.get(1);

        _performanceMetrics = performanceMetrics;
        _peers = agentFactory.createPeers(_trainData, configuration.peerCount, configuration.iterations, configuration.budget, configuration.parameters, configuration.epsilon);


        registerPeers(_peers);
    }

    private void registerPeers(List<PeerAgent> peers) throws StaleProxyException {
        for(PeerAgent peer : peers){
            _environment.registerAgent(peer);
            _peerGraph.join(peer, PeerAgent.SERVICE_NAME);
        }
    }

    public void run(Consumer<Experiment> completionAction) throws ControllerException {
        CompletionListeningAgent completionAgent = _agentFactory.getCompletionAgent(completionAction, _configuration.peerCount, this, _configuration.iterations);
        GroupLocatorAgent groupAgent = _agentFactory.getGroupLocatingAgentWithAgents(_peers, _configuration);
        _peerGraph.join(completionAgent, CompletionListeningAgent.SERVICE_NAME);
        _environment.registerAgent(completionAgent);
        _environment.registerAgent(groupAgent);
        _environment.run();
    }

    public List<Double> test() {
        return _peers.stream()
                .mapToDouble(peer -> _performanceMetrics.errorRate(_testData, peer.labelData(_testData)))
                .boxed()
                .collect(Collectors.toList());
    }

    public List<ConfusionMatrix> test2(){
        List<ConfusionMatrix> listOfConfusionMatrices = new ArrayList<ConfusionMatrix>();
        PeerAgent peerMin = null;
        PeerAgent peerMax = null;
        double max = 0.0;
        double min = 100.0;
        for(double threshold = 0.0; threshold <= 1.0; threshold = threshold + 0.01d) {
            for (PeerAgent peer : _peers) {

                ConfusionMatrix matrix = new ConfusionMatrix(_testData, peer.labelData(_testData, threshold), threshold);
                //matrix.printConfusionMatrix();

                if (matrix.getCorrectClassifiedPercentage() > max) {
                    max = matrix.getCorrectClassifiedPercentage();
                    peerMax = peer;
                }
                if (matrix.getCorrectClassifiedPercentage() < min) {
                    min = matrix.getCorrectClassifiedPercentage();
                    peerMin = peer;
                }

            }
        }

        ConfusionMatrix bestClassifier = new ConfusionMatrix(_testData,peerMax.labelData(_testData),0.5);
        ConfusionMatrix worstClassifier = new ConfusionMatrix(_testData,peerMin.labelData(_testData),0.5);
        
        listOfConfusionMatrices.add(bestClassifier);
        listOfConfusionMatrices.add(worstClassifier);



        ROC_Curve roc = new ROC_Curve(listOfConfusionMatrices, "peertest");
        roc.addPointToCurve();
        roc.writeFile();
        return listOfConfusionMatrices;
    }


    public List<LabeledSample> getData() {
        return _data;
    }

    public ExperimentConfiguration getConfiguration() {
        return _configuration;
    }

    public void reset() {
        _environment.clearAndKill();
    }
}
