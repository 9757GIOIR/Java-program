package Checkfile;

import com.spire.xls.ExcelVersion;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;

public class filecheck {

    public static void main(String[] args) {

        //Create a Workbook object
        Workbook workbook = new Workbook();

        //Load an Excel file containing protected worksheet
        workbook.loadFromFile("D:\\test1.xlsx");

        //Get the first worksheet
        Worksheet sheet = workbook.getWorksheets().get(0);

        //Unprotect the worksheet using the specified password
        sheet.unprotect("Pune@123");

        //Save the workbook to another Excel file
        workbook.saveToFile("D:\\UnprotectWorksheet.xlsx", ExcelVersion.Version2016);
    }
}