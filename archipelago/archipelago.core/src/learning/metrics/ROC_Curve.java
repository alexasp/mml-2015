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

    List<ConfusionMatrix> listOfConfusionMatrices;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    HSSFRow header;
    String _peerName;


    public ROC_Curve(List<ConfusionMatrix> matrices, String peerName){
        listOfConfusionMatrices=matrices;
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet("Sample sheet");
        header = sheet.createRow(0);
        header.createCell(0).setCellValue("Sensitivity");
        header.createCell(1).setCellValue("1-Specitivity");
        header.createCell(2).setCellValue("Threshold");
        _peerName=peerName;
    }

    public void formChartObject(){
        //Create a new row in current sheet
        int rownum = 1;

        for(ConfusionMatrix matrix : listOfConfusionMatrices) {
            HSSFRow row = sheet.createRow(rownum++);
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellValue(matrix.getSensitivity());
            HSSFCell cell2 = row.createCell(1);
            double minus = (1-matrix.getSpecitivity());
            cell2.setCellValue(minus);
            HSSFCell cell3 = row.createCell(2);
            cell3.setCellValue(matrix.getThreshold());
        }
    }


    public void calculateAUC(){

    }
    
    public void writeChartToFile(){

    // Write the output to a file
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(_peerName+".xls");
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
