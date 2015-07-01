package learning.metrics;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
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

    private void formChartObject(List<ConfusionMatrix> listOfConfusionMatrices){
        //Create a new row in current sheet
        int rownum = 1;
        double maxClassification = listOfConfusionMatrices.get(0).getCorrectClassifiedPercentage();
        double maxClassificationThreshold = listOfConfusionMatrices.get(0).getThreshold();

        ConfusionMatrix _bestConfusionMatrix = listOfConfusionMatrices.get(0);

        for(ConfusionMatrix matrix : listOfConfusionMatrices) {

            addRow(sheet, rownum, matrix.getSensitivity(), matrix.getFalsePositiveRate(), matrix.getThreshold());

            if(matrix.getCorrectClassifiedPercentage()>maxClassification){
                maxClassification=matrix.getCorrectClassifiedPercentage();
                maxClassificationThreshold = matrix.getThreshold();
                _bestConfusionMatrix=matrix;
            }

            rownum++;
        }

        addRow(sheet, rownum+2, "Best %","Threshold");
        addRow(sheet,rownum+3, maxClassification, maxClassificationThreshold);

        addRow(sheet, rownum+5, "TP", "FP", "FN", "TN");

        addRow(sheet, rownum+6, _bestConfusionMatrix.getTP(), _bestConfusionMatrix.getFP(), _bestConfusionMatrix.getFN(), _bestConfusionMatrix.getTN());

    }

    private void addRow(HSSFSheet sheet, int rownum, Object... cellValues) {
        HSSFRow expRow = sheet.createRow(rownum);

        for (int i = 0; i < cellValues.length; i++) {
            HSSFCell expCell = expRow.createCell(i);
            expCell.setCellValue(cellValues[i].toString());
        }
    }

    private void addRow(HSSFSheet sheet, int rownum, Double... cellValues) {
        HSSFRow expRow = sheet.createRow(rownum);

        for (int i = 0; i < cellValues.length; i++) {
            HSSFCell expCell = expRow.createCell(i);
            expCell.setCellValue(String.format("%.4f", cellValues[i]));
        }
    }


    public void calculateAUC(){

    }

    public void writeChartToFile(){

    // Write the output to a file

        PrintStream fileOut = null;
        try {
            fileOut = new PrintStream(_path + ".csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to write experimental output!", e);
        }

        for (Row cells : sheet) {
            ArrayList<String> cellList = new ArrayList<>();
            cells.forEach(cell -> cellList.add(cell.getStringCellValue()));

            String line = String.join(",", cellList);
            fileOut.println(line);
        }

        fileOut.close();


    }

}
