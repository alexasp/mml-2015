package application.experiments;

import application.AppInjector;
import application.ConfigurationModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import experiment.DataLoader;
import experiment.Experiment;
import experiment.ExperimentConfiguration;
import experiment.ExperimentFactory;
import jade.wrapper.ControllerException;
import learning.LabeledSample;

import java.util.Collections;
import java.util.List;

/**
 * Created by aspis on 25.03.2015.
 */
public class SpamTest {


    public static void main(String[] args) throws ControllerException {
        Injector injector = Guice.createInjector(new AppInjector());

        DataLoader loader = injector.getInstance(DataLoader.class);
        List<LabeledSample> data = loader.readCSVFileReturnSamples("../data/uci_spambase_centered.csv", "start", true); //this is test leakage. Centering should be performed based on train data only
        Collections.shuffle(data);

        double trainRatio = 0.08;
        int peerCount = 10;
        int groupSize = 3;
        double testCost = 0.1;
        int iterations = 10;
        double regularization = 1.0;
        double budget = 1.0;
        int parameters = data.get(0).getFeatures().length;
        double updateCost = 0.01;

        ExperimentConfiguration configuration = new ExperimentConfiguration(iterations, budget, trainRatio, peerCount, testCost, parameters, updateCost, regularization, groupSize);
        injector = injector.createChildInjector(new ConfigurationModule(configuration));

        ExperimentFactory experimentFactory = injector.getInstance(ExperimentFactory.class);
        Experiment experiment = experimentFactory.getExperiment(data, configuration);

        experiment.run(completeExperiment -> System.out.println(completeExperiment.test()));
    }

}
