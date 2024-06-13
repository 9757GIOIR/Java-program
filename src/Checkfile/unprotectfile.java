package Checkfile;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
 
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
 
 
public class unprotectfile {
 
    public static void main(String[] args) {
        String excelFilePath = "D:\\test1.xlsx";
        String password = "Pune@123";
         
        try {
            Workbook workbook = WorkbookFactory.create(new File(excelFilePath), password);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();
 
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
 
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
 
                    switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        System.out.print(cell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        System.out.print(cell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        System.out.print(cell.getNumericCellValue());
                        break;
                    }
                    System.out.print("\t");
                }
                 
                
                System.out.println("Workbook:+");
            }
            
            workbook.close();
           
            
        } catch (EncryptedDocumentException | IOException
                | InvalidFormatException ex) {
            ex.printStackTrace();
        }
    }
}