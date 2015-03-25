package application.experiments;

import application.AppInjector;
import com.google.inject.Guice;
import com.google.inject.Injector;
import experiment.DataLoader;
import experiment.Experiment;
import experiment.ExperimentFactory;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import learning.LabeledSample;
import privacy.Budget;
import privacy.NoisyQueryable;
import privacy.NoisyQueryableFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aspis on 25.03.2015.
 */
public class SpamTest {


    public static void main(String[] args) throws ControllerException {
        Injector injector = Guice.createInjector(new AppInjector());

        DataLoader loader = injector.getInstance(DataLoader.class);
        ExperimentFactory experimentFactory = injector.getInstance(ExperimentFactory.class);
        NoisyQueryableFactory queryableFactory = injector.getInstance(NoisyQueryableFactory.class);

        List<LabeledSample> data = loader.readCSVFileReturnSamples("spamorham.csv");
        NoisyQueryable<LabeledSample> secureData = queryableFactory.getQueryable(new Budget(5.0), data);

        double trainRatio = 0.8;
        int peerCount = 5;
        double testCost = 0.1;
        int iterations = 10;

        Experiment experiment = experimentFactory.getExperiment(secureData, trainRatio, peerCount, testCost, iterations);

        experiment.run(completeExperiment -> System.out.println(completeExperiment.test()));
    }

}
