package experiment;

import com.google.common.collect.Lists;
import com.opencsv.CSVReader;
import learning.LabeledSample;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Created by peterfh on 25.03.2015.
 */
public class DataLoader {
    private String [] nextLine;
    private String label;
    private double numericLabel;

    public List<LabeledSample> readCSVFileReturnSamples(String filename, int labelIndex,Boolean isNumericLabel) {
        List<LabeledSample> listOfSamples = new ArrayList<LabeledSample>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filename));

            while ((nextLine = reader.readNext()) != null) {
                label = nextLine[labelIndex];

                if(isNumericLabel==false){
                    numericLabel = label == "M" ? 1.0 : -1.0;
                }
                else{
                    numericLabel = Double.parseDouble(label);
                    numericLabel = numericLabel == 0.0 ? -1.0 : numericLabel;
                }

                double[] vector = new double[nextLine.length-1 +1];
                int counter = 0;
                for (int i = 0; i < nextLine.length; i++)
                {
                    if(i != labelIndex) {
                        vector[counter] = Double.parseDouble(nextLine[i]);
                        counter++;
                    }
                }

                vector[vector.length-1] = 1.0; //bias term

                LabeledSample sample = new LabeledSample(numericLabel,vector);
                listOfSamples.add(sample);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfSamples;

    }

    public static List<List<LabeledSample>> partition(int parts, List<LabeledSample> data) {
        if(parts == 1){
            return new ArrayList<>(Arrays.asList(data));
        }

        int recordsToEach = (int) Math.floor((double) data.size() / (double) parts);

        List<List<LabeledSample>> partitions = IntStream.range(0, parts).mapToObj(i -> new ArrayList<LabeledSample>()).collect(Collectors.toList());
        int index = 0;
        int targetPartition = 0;
        for (int i = 0; i < data.size(); i++) {
            index++;

            partitions.get(targetPartition).add(data.get(i));


            if((i + 1) % recordsToEach == 0){
                targetPartition++;
            }
            if(targetPartition >= parts){
                break;
            }
        }

        for(int i = index; i < data.size(); i++){
            partitions.get((i - index)).add(data.get(index));
        }

        return partitions;
    }

    public static List<List<LabeledSample>> partition(double trainRatio, List<LabeledSample> data) {
        List<List<LabeledSample>> partitions = new ArrayList<>();
        int target = (int)(trainRatio * (double)data.size());

        List<LabeledSample> first = new ArrayList<>();
        List<LabeledSample> second = new ArrayList<>();

        for(int i = 0; i < data.size(); i++){
            if(i < target){
                first.add(data.get(i));
            }
            else {
                second.add(data.get(i));
            }
        }
        partitions.add(first);
        partitions.add(second);

        return partitions;
    }

    public List<List<LabeledSample>> partition(int parts, List<LabeledSample> data, int recordsPerPeer) {
        List<List<LabeledSample>> partitioned = Lists.partition(data, recordsPerPeer);
        return partitioned.subList(0, parts);
    }

    public static List<LabeledSample> mergeExcept(List<List<LabeledSample>> folds, int excludedFold) {
        List<LabeledSample> mergedList = new ArrayList<>();

        for (int i = 0; i < folds.size(); i++) {
            if (i != excludedFold) {
                mergedList.addAll(folds.get(i));
            }
        }

        return mergedList;
    }


}
