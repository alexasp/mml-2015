package application.experiments;

import application.AppInjector;
import com.google.inject.Guice;
import com.google.inject.Injector;
import experiment.DataLoader;
import experiment.Experiment;
import experiment.ExperimentFactory;
import jade.wrapper.ControllerException;
import learning.IQueryableFactory;
import learning.LabeledSample;
import learning.QueryableFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by aspis on 25.03.2015.
 */
public class SpamTest {


    public static void main(String[] args) throws ControllerException {
        Injector injector = Guice.createInjector(new AppInjector());

        DataLoader loader = injector.getInstance(DataLoader.class);
        ExperimentFactory experimentFactory = injector.getInstance(ExperimentFactory.class);
        IQueryableFactory queryableFactory = injector.getInstance(IQueryableFactory.class);

        List<LabeledSample> data = loader.readCSVFileReturnSamples("../data/uci_spambase_centered.csv","start",true); //this is test leakage. Centering should be performed based on train data only
        Collections.shuffle(data);

        double trainRatio = 0.08;
        int peerCount = 1;
        double testCost = 0.1;
        int iterations = 1;

        Experiment experiment = experimentFactory.getExperiment(data, trainRatio, peerCount, testCost, iterations, 2.0, data.get(0).getFeatures().length, 0.01);

        experiment.run(completeExperiment -> System.out.println(completeExperiment.test()));
    }

}
