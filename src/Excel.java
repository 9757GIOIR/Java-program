import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class Excel {
    public static void main(String[] args) {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();

        // Create a sheet within the workbook
        Sheet sheet = workbook.createSheet("Sheet1");

        // Create a row in the sheet
        Row row = sheet.createRow(0);

        // Create cells in the row
        Cell cell1 = row.createCell(0);
        cell1.setCellValue("Hello");

        Cell cell2 = row.createCell(1);
        cell2.setCellValue("World");

        // Write the workbook to a file
        try (FileOutputStream fileOut = new FileOutputStream("D:\\example.xlsx")) {
            workbook.write(fileOut);
            System.out.println("Excel file created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
