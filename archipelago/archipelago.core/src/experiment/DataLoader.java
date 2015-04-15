package experiment;

import com.google.common.collect.Lists;
import com.opencsv.CSVReader;
import learning.LabeledSample;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by peterfh on 25.03.2015.
 */
public class DataLoader {
    private String [] nextLine;
    private String label;
    private double numericLabel;

    public List<LabeledSample> readCSVFileReturnSamples(String filename, String labelPos,Boolean isNumericLabel) {
        List<LabeledSample> listOfSamples = new ArrayList<LabeledSample>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filename));

            while ((nextLine = reader.readNext()) != null) {
                if(labelPos=="end"){
                     label = nextLine[nextLine.length-1];
                }
                else{
                     label = nextLine[0];
                }
                if(isNumericLabel==false){
                    numericLabel = label == "M" ? 1.0 : -1.0;
                }
                else{
                    numericLabel = Double.parseDouble(label) * 2.0 - 1.0;
                }

                double[] vector = new double[nextLine.length-1 +1];
                for (int i = 1; i < nextLine.length; i++)
                {
                    vector[i-1]= Double.parseDouble(nextLine[i]);
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

    public List<List<LabeledSample>> partition(int parts, List<LabeledSample> data) {
        int recordsToEach = (int) Math.ceil((double)data.size()/(double)parts);
        return Lists.partition(data, recordsToEach);
    }

    public List<List<LabeledSample>> partition(double trainRatio, List<LabeledSample> data) {
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

       /*
        try {
        FileInputStream inputStream = new FileInputStream();
        UTF8_CHARSET charset = StandardCharsets.UTF_8.newDecoder();
        UTF8_CHARSET.onMalformedInput(CodingErrorAction.REPLACE);
        FileReader fileReader = new InputStreamReader(inputStream, UTF8_CHARSET);
        this.reader = new CSVReader(this.fileReader);
        }
        catch (FileNotFoundException ex) {

        }
        */

}
