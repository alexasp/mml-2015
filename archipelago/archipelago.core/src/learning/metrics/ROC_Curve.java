package learning.metrics;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * Created by peterfh on 27.04.2015.
 */


public class ROC_Curve {

    private final String _path;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    HSSFRow header;
    String _experimentName;


    public ROC_Curve(String path,String experimentName)
    {
        _path = path;
        workbook = new HSSFWorkbook();
        _experimentName = experimentName;
    }

    public void add(List<ConfusionMatrix> matrices, String peerName){

        sheet = workbook.createSheet(peerName);
        header = sheet.createRow(0);
        header.createCell(0).setCellValue("Sensitivity");
        header.createCell(1).setCellValue("False Positive Rate");
        header.createCell(2).setCellValue("Threshold");
        formChartObject(matrices);

    }

    public void formChartObject(List<ConfusionMatrix> listOfConfusionMatrices){
        //Create a new row in current sheet
        int rownum = 1;
        double maxClassification = 0.0d;
        double maxClassificationThreshold = 0.0d;

        for(ConfusionMatrix matrix : listOfConfusionMatrices) {
            HSSFRow row = sheet.createRow(rownum++);
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellValue(matrix.getSensitivity());
            HSSFCell cell2 = row.createCell(1);
            double minus = (matrix.getFalsePositiveRate());
            cell2.setCellValue(minus);
            HSSFCell cell3 = row.createCell(2);
            cell3.setCellValue(matrix.getThreshold());

//            if(matrix.getCorrectClassifiedPercentage()>maxClassification){
//                maxClassification=matrix.getCorrectClassifiedPercentage();
//                maxClassificationThreshold = matrix.getThreshold();
//            }
        }
    }
    public void getBestConfusionMatrix(){

    }


    public void calculateAUC(){

    }

    public void writeChartToFile(){

    // Write the output to a file
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(_experimentName+".xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
