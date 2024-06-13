package Checkfile;

import com.spire.xls.ExcelVersion;
import com.spire.xls.Workbook;

public class read {

    public static void main(String[] args) {

        //Create a Workbook object
        Workbook workbook = new Workbook();

        //Specify the open password
        workbook.setOpenPassword("Pune@123");

        //Load an encrypted Excel file
        workbook.loadFromFile("D:\\test1.xlsx");

        //Unprotect workbook
        workbook.unProtect();

        //Reset password
        //workbook.protect("newpassword");

        //Save the workbook to another Excel file
        workbook.saveToFile("D:\\Unprotect.xlsx", ExcelVersion.Version2016);
    }
}