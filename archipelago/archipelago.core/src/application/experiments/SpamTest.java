package application.experiments;

import application.AppInjector;
import application.ConfigurationModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import experiment.DataLoader;
import experiment.Experiment;
import experiment.ExperimentConfiguration;
import experiment.ExperimentFactory;
import jade.wrapper.ControllerException;
import learning.LabeledSample;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * Created by aspis on 25.03.2015.
 */
public class SpamTest {


    public static void main(String[] args) throws ControllerException, InterruptedException {
        runExperiment();
    }

    private static void runExperiment() throws ControllerException, InterruptedException {
        Injector injector = Guice.createInjector(new AppInjector());

        DataLoader loader = injector.getInstance(DataLoader.class);
        List<LabeledSample> data = loader.readCSVFileReturnSamples("../data/uci_spambase_centered.csv", "start", true); //this is test leakage. Centering should be performed based on train data only
        Collections.shuffle(data);

        double trainRatio = 0.8;
        int peerCount = 100;
        int groupSize = 20;
        double testCost = 0.1;
        int iterations = 10;
        double regularization = 1.0;
        double budget = 1.0;
        int parameters = data.get(0).getFeatures().length;
        double epsilon = 10;

        ExperimentConfiguration configuration = new ExperimentConfiguration(iterations, budget, trainRatio, peerCount, testCost, parameters, epsilon, regularization, groupSize);
        injector = injector.createChildInjector(new ConfigurationModule(configuration));

        ExperimentFactory experimentFactory = injector.getInstance(ExperimentFactory.class);
        Experiment experiment = experimentFactory.getExperiment(data, configuration);

        //experiment.run(completeExperiment -> System.out.println(completeExperiment.test));

//        try(Scanner sc = new Scanner(System.in)) {
//            String input = sc.nextLine();
//        }

        CountDownLatch latch = new CountDownLatch(1);

        Consumer<Experiment> completionAction = completeExperiment -> {
            System.out.println(meanstd(completeExperiment.test()));
            experiment.test2();
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
