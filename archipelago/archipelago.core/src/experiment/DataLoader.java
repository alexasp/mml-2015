package experiment;

import com.opencsv.CSVReader;
import learning.LabeledSample;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by peterfh on 25.03.2015.
 */
public class DataLoader {

    public List<LabeledSample> readCSVFileReturnSamples(String filename) {
        List<LabeledSample> listOfSamples = new ArrayList<LabeledSample>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filename));
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String label = nextLine[0];
                double numericLabel = label == "M" ? 1.0 : -1.0;
                double[] vector = new double[nextLine.length-1];
                for (int i = 1; i < nextLine.length; i++)
                {
                    vector[i-1]= Double.parseDouble(nextLine[i]);
                }
                LabeledSample sample = new LabeledSample(numericLabel,vector);
                listOfSamples.add(sample);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfSamples;

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
