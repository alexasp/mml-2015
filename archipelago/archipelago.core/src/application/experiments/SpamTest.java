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

import java.util.List;

/**
 * Created by aspis on 25.03.2015.
 */
public class SpamTest {


    public static void main(String[] args) throws ControllerException {
        Injector injector = Guice.createInjector(new AppInjector());

        DataLoader loader = injector.getInstance(DataLoader.class);
        ExperimentFactory experimentFactory = injector.getInstance(ExperimentFactory.class);
        IQueryableFactory queryableFactory = injector.getInstance(QueryableFactory.class);

        List<LabeledSample> data = loader.readCSVFileReturnSamples("../data/uci_spambase.csv","end",true);

        double trainRatio = 0.8;
        int peerCount = 5;
        double testCost = 0.1;
        int iterations = 10;

        Experiment experiment = experimentFactory.getExperiment(data, trainRatio, peerCount, testCost, iterations, 2.0, data.get(0).getFeatures().length, 0.01);

        experiment.run(completeExperiment -> System.out.println(completeExperiment.test()));
    }

}
