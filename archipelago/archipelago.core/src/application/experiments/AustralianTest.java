package application.experiments;

import application.AppInjector;
import application.ExperimentModule;
import application.experiments.results.Reporting;
import com.google.inject.Guice;
import com.google.inject.Injector;
import communication.peer.behaviours.aggregation.PublishTypes;
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
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.max;

/**
 * Created by peterfh on 14.05.2015.
 */

    /**
     * Created by aspis on 25.03.2015.
     */
    public class AustralianTest {

        public static void main(String[] args) throws ControllerException, InterruptedException, IOException {

            List<LabeledSample> trainData;
            List<LabeledSample> testData;

            //      trainData = new DataLoader().readCSVFileReturnSamples("../data/uci_spambase.csv.train", 57, true);
//      testData = new DataLoader().readCSVFileReturnSamples("../data/uci_spambase.csv.test", 57, true);

            trainData = new DataLoader().readCSVFileReturnSamples("../data/australian_test_fixed.csv.train", 14, true);
            testData = null;
//      testData = new DataLoader().readCSVFileReturnSamples("../data/australian_test_fixed.csv", 14, true);

            PublishTypes modelPublishType = PublishTypes.All;
            boolean useCrossValidation = true;
            int foldCount = 10;

            if(useCrossValidation){
                testData = null;
            }

            List<Integer> peerCounts = Arrays.asList(10);
            List<Integer> groupSizes = Arrays.asList(2,3,4,5,6,7,8,9,10);

            //List<PrivacyParam> privacyParams = IntStream.range(-6, 2).mapToObj(i -> PrivacyParam.get(Math.pow(2, i), Math.pow(2, i))).collect(Collectors.toList());
            List<Double> regularizations = IntStream.range(-1,0).mapToDouble(i->Math.pow(2, i)).boxed().collect(Collectors.toList());
            List<PrivacyParam> privacyParams = Arrays.asList(
                    new PrivacyParam(0.01, 0.01)

            );

            double trainDataSize = useCrossValidation ? (double) trainData.size() / (double)foldCount * ((double)foldCount - 1.0) : trainData.size();
            int recordsPerPeer = (int) ( trainDataSize / (double) max(peerCounts));
            System.out.println("Total number of records per peer:" + recordsPerPeer);

            int parameters = trainData.get(0).getFeatures().length;

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

                            ExperimentConfiguration configuration = new ExperimentConfiguration(aggregations, privacyParam.perUpdateBudget, peerCount, parameters, privacyParam.epsilon, regularization, groupSize, recordsPerPeer, foldCount, useCrossValidation);
                            configuration.publishType = modelPublishType;
                            testWithParameters(peerCount, groupSize, trainData, testData, recordsPerPeer, injector, configuration);
                        }
                    }
                }
            }

            System.exit(0);
        }

        private static void testWithParameters(Integer peerCount, Integer groupSize, List<LabeledSample> trainDataSource, List<LabeledSample> testDataSource, int recordsPerPeer, Injector injector, ExperimentConfiguration configuration) throws ControllerException, InterruptedException {

            System.out.println(String.format("Running with peerCount %s, groupSize %s, epsilon %s, aggregation_cost %s, regularization %s", peerCount, groupSize, configuration.epsilon, configuration.updateCost, configuration.regularization));
            Collections.shuffle(trainDataSource);
            List<List<LabeledSample>> folds = DataLoader.partition(configuration.cvFolds, trainDataSource);


            for(int i = 0; i < 10; i++) {
                List<LabeledSample> train;
                List<LabeledSample> test;

                if(configuration.useCrossValidation) {
                    train = DataLoader.mergeExcept(folds, i);
                    test = folds.get(i);
                }
                else{
                    train = trainDataSource;
                    Collections.shuffle(train);
                    test = testDataSource;
                }
//            int peerCount = 100;
//            int groupSize = 20;



                Injector currentInjector = injector.createChildInjector(new ExperimentModule(configuration, new CountDownLatch(peerCount)));

                ExperimentFactory experimentFactory = currentInjector.getInstance(ExperimentFactory.class);
                Experiment experiment = experimentFactory.getExperiment(train,test, configuration);

                runExperiment(experiment, String.format(Locale.US, "eps,%.8f-regularization,%.8f-cost,%.3f-peers,%d-groups,%d", configuration.epsilon, configuration.regularization, configuration.updateCost, peerCount, groupSize, i), i);
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

