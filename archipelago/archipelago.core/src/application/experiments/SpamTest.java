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


//        List<Integer> peerCounts = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
//        List<Integer> groupSizes = Arrays.asList(2, 10, 20, 30, 40, 50, 60, 70, 80);



        List<Integer> peerCounts = Arrays.asList(500);
        List<Integer> groupSizes = Arrays.asList(10, 50, 80, 100, 200);
        int recordsPerPeer = (int) (trainRatio * (double) data.size() / (double) max(peerCounts));
        System.out.println("Total number of records per peer:" + recordsPerPeer);

        for (Integer peerCount : peerCounts) {
            for (Integer groupSize : groupSizes) {
                if(groupSize > peerCount){ continue; }

                testWithParameters(peerCount, groupSize, data, recordsPerPeer, trainRatio);
            }
        }

        System.exit(0);
    }

    private static void testWithParameters(Integer peerCount, Integer groupSize, List<LabeledSample> data, int recordsPerPeer, double trainRatio) throws ControllerException, InterruptedException {
        for(int i = 0; i < 10; i++) {

            //todo: use recordsPerPeer limitation

            Injector injector = Guice.createInjector(new AppInjector());

            DataLoader loader = injector.getInstance(DataLoader.class);
            Collections.shuffle(data);

//            int peerCount = 100;
//            int groupSize = 20;
            double testCost = 0.1;
            double regularization = 10.0;
            double perUpdateBudget = 0.1d;
            int parameters = data.get(0).getFeatures().length;
            double epsilon = 0.1d;
            int aggregations = (int)(epsilon/perUpdateBudget*peerCount/groupSize);

            ExperimentConfiguration configuration = new ExperimentConfiguration(aggregations, perUpdateBudget, trainRatio, peerCount, testCost, parameters, epsilon, regularization, groupSize, recordsPerPeer);
            injector = injector.createChildInjector(new ExperimentModule(configuration, new CountDownLatch(peerCount)));

            ExperimentFactory experimentFactory = injector.getInstance(ExperimentFactory.class);
            Experiment experiment = experimentFactory.getExperiment(data, configuration);

            runExperiment(experiment, String.format("eps%.3f-reg%.3f-cost%.3f-peers%d-groups%d", epsilon, regularization, perUpdateBudget, peerCount, groupSize, i), i);
        }
    }

    private static void runExperiment(Experiment experiment, String experimentName, int iteration) throws ControllerException, InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        Consumer<Experiment> completionAction = completeExperiment -> {
            Reporting.writeTestResults(experiment, experimentName, iteration);
            experiment.reset();
            latch.countDown();
        };

        experiment.run(completionAction);
        latch.await();
    }


}
