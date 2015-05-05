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


    public ROC_Curve(String path)
    {
        _path = path;
        workbook = new HSSFWorkbook();
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
        double maxClassification = listOfConfusionMatrices.get(0).getCorrectClassifiedPercentage();
        double maxClassificationThreshold = listOfConfusionMatrices.get(0).getThreshold();

        ConfusionMatrix _bestConfusionMatrix = listOfConfusionMatrices.get(0);

        for(ConfusionMatrix matrix : listOfConfusionMatrices) {
            HSSFRow row = sheet.createRow(rownum++);
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellValue(matrix.getSensitivity());
            HSSFCell cell2 = row.createCell(1);
            double minus = (matrix.getFalsePositiveRate());
            cell2.setCellValue(minus);
            HSSFCell cell3 = row.createCell(2);
            cell3.setCellValue(matrix.getThreshold());

            if(matrix.getCorrectClassifiedPercentage()>maxClassification){
                maxClassification=matrix.getCorrectClassifiedPercentage();
                maxClassificationThreshold = matrix.getThreshold();
                _bestConfusionMatrix=matrix;
            }
        }
        HSSFRow jumpRow = sheet.createRow(rownum+2);
        HSSFCell cell1 =jumpRow.createCell(0);
        cell1.setCellValue("Best %,Threshold");
        HSSFCell cell2 = jumpRow.createCell(1);
        cell2.setCellValue(maxClassification);
        HSSFCell cell3 = jumpRow.createCell(2);
        cell3.setCellValue(maxClassificationThreshold);

        HSSFRow expRow = sheet.createRow(rownum+3);
        HSSFCell expCell1 = expRow.createCell(0);
        expCell1.setCellValue("TP");
        HSSFCell expCell2 = expRow.createCell(1);
        expCell2.setCellValue("FP");
        HSSFCell expCell3 = expRow.createCell(2);
        expCell3.setCellValue("FN");
        HSSFCell expCell4 = expRow.createCell(3);
        expCell4.setCellValue("TN");

        HSSFRow lastRow = sheet.createRow(rownum+4);
        HSSFCell lastCell1 = lastRow.createCell(0);
        lastCell1.setCellValue(_bestConfusionMatrix.getTP());
        HSSFCell lastCell2 = lastRow.createCell(1);
        lastCell2.setCellValue(_bestConfusionMatrix.getFP());
        HSSFCell lastCell3 = lastRow.createCell(2);
        lastCell3.setCellValue(_bestConfusionMatrix.getFN());
        HSSFCell lastCell4 = lastRow.createCell(3);
        lastCell4.setCellValue(_bestConfusionMatrix.getTN());


    }


    public void calculateAUC(){

    }

    public void writeChartToFile(){

    // Write the output to a file
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(_path+".xls");
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
