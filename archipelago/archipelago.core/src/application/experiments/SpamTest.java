package application.experiments;

import application.AppInjector;
import application.ExperimentModule;
import application.experiments.results.Reporting;
import com.google.inject.Guice;
import com.google.inject.Injector;
import experiment.DataLoader;
import experiment.Experiment;
import experiment.ExperimentConfiguration;
import experiment.ExperimentFactory;
import jade.wrapper.ControllerException;
import learning.LabeledSample;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static java.util.Collections.max;

/**
 * Created by aspis on 25.03.2015.
 */
public class SpamTest {

    public static void main(String[] args) throws ControllerException, InterruptedException, IOException {
        List<LabeledSample> data = new DataLoader().readCSVFileReturnSamples("../data/uci_spambase_centered.csv", "start", true); //this is test leakage. Centering should be performed based on train data only
        double trainRatio = 0.8;

        List<Integer> peerCounts = Arrays.asList(100);
        List<Integer> groupSizes = Arrays.asList(10);
        List<PrivacyParam> privacyParams = Arrays.asList(
                PrivacyParam.get(0.0001, 0.0001/4.0),
                PrivacyParam.get(0.001, 0.001/4.0),
                PrivacyParam.get(0.01, 0.01/4.0),
                PrivacyParam.get(0.1, 0.1/4.0),
                PrivacyParam.get(1.0, 1.0/4.0),
                PrivacyParam.get(10.0, 10.0/4.0),
                PrivacyParam.get(100.0, 100.0/4.0));
//        List<Integer> peerCounts = Arrays.asList(500);
//        List<Integer> groupSizes = Arrays.asList(50);


        int recordsPerPeer = (int) (trainRatio * (double) data.size() / (double) max(peerCounts));
        System.out.println("Total number of records per peer:" + recordsPerPeer);


        double testCost = 0.1;
        double regularization = 10;
        int parameters = data.get(0).getFeatures().length;

        Injector injector = Guice.createInjector(new AppInjector());

        for (Integer peerCount : peerCounts) {
            for (Integer groupSize : groupSizes) {
                if (groupSize > peerCount) {
                    continue;
                }

                for(PrivacyParam privacyParam : privacyParams) {
                    int aggregations = (int) (privacyParam.epsilon / privacyParam.perUpdateBudget * (peerCount - groupSize + 1) / groupSize);
                    aggregations = aggregations == 0 ? 1 : aggregations;

                    ExperimentConfiguration configuration = new ExperimentConfiguration(aggregations, privacyParam.perUpdateBudget, trainRatio, peerCount, testCost, parameters, privacyParam.epsilon, regularization, groupSize, recordsPerPeer);

                    testWithParameters(peerCount, groupSize, data, recordsPerPeer, trainRatio, injector, configuration);
                }
            }
        }

        System.exit(0);
    }

    private static void testWithParameters(Integer peerCount, Integer groupSize, List<LabeledSample> data, int recordsPerPeer, double trainRatio, Injector injector, ExperimentConfiguration configuration) throws ControllerException, InterruptedException {

        System.out.println(String.format("Running with peerCount %s, groupSize %s, epsilon %s, aggregation_cost %s", peerCount, groupSize, configuration.epsilon, configuration.perUpdateBudget));

        for(int i = 0; i < 10; i++) {
            Collections.shuffle(data);

//            int peerCount = 100;
//            int groupSize = 20;

            Injector currentInjector = injector.createChildInjector(new ExperimentModule(configuration, new CountDownLatch(peerCount)));

            ExperimentFactory experimentFactory = currentInjector.getInstance(ExperimentFactory.class);
            Experiment experiment = experimentFactory.getExperiment(data, configuration);

            runExperiment(experiment, String.format("eps,%.3f-reg,%.3f-cost,%.3f-peers,%d-groups,%d", configuration.epsilon, configuration.regularization, configuration.perUpdateBudget, peerCount, groupSize, i), i);
        }
    }

    private static void runExperiment(Experiment experiment, String experimentName, int iteration) throws ControllerException, InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        Consumer<Experiment> completionAction = completeExperiment -> {
            Reporting.writeTestResults(experiment, experimentName, iteration);
            latch.countDown();
        };

        experiment.run(completionAction);
        latch.await();
    }


}
