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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.max;

/**
 * Created by aspis on 25.03.2015.
 */
public class SpamTest {

    public static void main(String[] args) throws ControllerException, InterruptedException, IOException {

        List<LabeledSample> data = new DataLoader().readCSVFileReturnSamples("../data/uci_spambase_centered.csv", "start", true); //this is test leakage. Centering should be performed based on train data only

//        List<LabeledSample> data = new DataLoader().readCSVFileReturnSamples("../data/australian_test_fixed.csv", "end", true); //this is test leakage. Centering should be performed based on train data only
        double trainRatio = 0.8;

        List<Integer> peerCounts = Arrays.asList(100);
        List<Integer> groupSizes = Arrays.asList(20);
        List<PrivacyParam> privacyParams = IntStream.range(-6, 7).mapToObj(i -> PrivacyParam.get(Math.pow(2, i), Math.pow(2, i))).collect(Collectors.toList());
        List<Double> regularizations = IntStream.range(2, 3).mapToDouble(i->Math.pow(2, i)).boxed().collect(Collectors.toList());
//        List<Integer> peerCounts = Arrays.asList(500);
//        List<Integer> groupSizes = Arrays.asList(50);


        int recordsPerPeer = (int) (trainRatio * (double) data.size() / (double) max(peerCounts));
        System.out.println("Total number of records per peer:" + recordsPerPeer);

        int parameters = data.get(0).getFeatures().length;

        Injector injector = Guice.createInjector(new AppInjector());

        for (Integer peerCount : peerCounts) {
            for (Integer groupSize : groupSizes) {
                if (groupSize > peerCount) {
                    continue;
                }

                for(PrivacyParam privacyParam : privacyParams) {
                    for(double regularization : regularizations) {
                        int aggregations = (int) (privacyParam.epsilon / privacyParam.perUpdateBudget * (peerCount - groupSize + 1) / groupSize);
                        aggregations = aggregations == 0 ? 1 : aggregations;

                        ExperimentConfiguration configuration = new ExperimentConfiguration(aggregations, privacyParam.perUpdateBudget, trainRatio, peerCount, parameters, privacyParam.epsilon, regularization, groupSize, recordsPerPeer);

                        testWithParameters(peerCount, groupSize, data, recordsPerPeer, trainRatio, injector, configuration);
                    }
                }
            }
        }

        System.exit(0);
    }

    private static void testWithParameters(Integer peerCount, Integer groupSize, List<LabeledSample> data, int recordsPerPeer, double trainRatio, Injector injector, ExperimentConfiguration configuration) throws ControllerException, InterruptedException {

        System.out.println(String.format("Running with peerCount %s, groupSize %s, epsilon %s, aggregation_cost %s, regularization %s", peerCount, groupSize, configuration.epsilon, configuration.updateCost, configuration.regularization));

        for(int i = 0; i < 10; i++) {
            Collections.shuffle(data);

//            int peerCount = 100;
//            int groupSize = 20;

            Injector currentInjector = injector.createChildInjector(new ExperimentModule(configuration, new CountDownLatch(peerCount)));

            ExperimentFactory experimentFactory = currentInjector.getInstance(ExperimentFactory.class);
            Experiment experiment = experimentFactory.getExperiment(data, configuration);

            runExperiment(experiment, String.format("eps,%.8f-regularization,%.8f-cost,%.3f-peers,%d-groups,%d", configuration.epsilon, configuration.regularization, configuration.updateCost, peerCount, groupSize, i), i);
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
