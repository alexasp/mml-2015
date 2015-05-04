package application.experiments;

import application.AppInjector;
import application.ExperimentModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import experiment.DataLoader;
import experiment.Experiment;
import experiment.ExperimentConfiguration;
import experiment.ExperimentFactory;
import jade.wrapper.ControllerException;
import learning.LabeledSample;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

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
            int peerCount = 10;
            int groupSize = 5;
            double testCost = 0.1;
            double regularization = 1.0;
            double perUpdateBudget = 0.05d;
            int parameters = data.get(0).getFeatures().length;
            double epsilon = 0.11d;
            int iterations = (int)(epsilon/perUpdateBudget*peerCount/groupSize);

            ExperimentConfiguration configuration = new ExperimentConfiguration(iterations, perUpdateBudget, trainRatio, peerCount, testCost, parameters, epsilon, regularization, groupSize);
            injector = injector.createChildInjector(new ExperimentModule(configuration, new CountDownLatch(peerCount)));

            ExperimentFactory experimentFactory = injector.getInstance(ExperimentFactory.class);
            Experiment experiment = experimentFactory.getExperiment(data, configuration);


            runExperiment(experiment);
        }
    }

    private static void runExperiment(Experiment experiment) throws ControllerException, InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        Consumer<Experiment> completionAction = completeExperiment -> {
            System.out.println(meanstd(completeExperiment.test()));
//            experiment.test2();
            experiment.reset();
            latch.countDown();
        };

        experiment.run(completionAction);
        latch.await();
    }

    private static String meanstd(List<Double> test) {
        double mean = test.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
        double std = test.stream().mapToDouble(error -> Math.pow(error-mean, 2)).average().getAsDouble();
        std = Math.sqrt(std);

        return String.format("Mean error: %f stddev: %f", mean, std);

    }

}
