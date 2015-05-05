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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static com.google.common.math.DoubleMath.mean;
import static java.util.Collections.max;
import static java.util.Collections.min;

/**
 * Created by aspis on 25.03.2015.
 */
public class SpamTest {


    public static void main(String[] args) throws ControllerException, InterruptedException, IOException {
        for(int i = 0; i < 3; i++) {

            Injector injector = Guice.createInjector(new AppInjector());

            DataLoader loader = injector.getInstance(DataLoader.class);
            List<LabeledSample> data = loader.readCSVFileReturnSamples("../data/uci_spambase_centered.csv", "start", true); //this is test leakage. Centering should be performed based on train data only
            Collections.shuffle(data);

            double trainRatio = 0.8;
            int peerCount = 100;
            int groupSize = 20;
            double testCost = 0.1;
            double regularization = 10.0;
            double perUpdateBudget = 0.05d;
            int parameters = data.get(0).getFeatures().length;
            double epsilon = 0.1d;
            int aggregations = (int)(epsilon/perUpdateBudget*peerCount/groupSize);

            ExperimentConfiguration configuration = new ExperimentConfiguration(aggregations, perUpdateBudget, trainRatio, peerCount, testCost, parameters, epsilon, regularization, groupSize);
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
