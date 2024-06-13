import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
 
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.aspose.cells.LoadFormat;
import com.aspose.cells.LoadOptions;
import com.aspose.cells.Workbook;
 
public class passRemover {
 
    public static void main(String[] args) throws Exception {
 
    	// Initialize loading options
    	LoadOptions loadOptions = new LoadOptions(LoadFormat.XLSX);

    	// Set original password
    	loadOptions.setPassword("Pune@123");

    	// Instantiate a Workbook object with Excel file's path
    	Workbook workbook = new Workbook("D:\\test1.xlsx", loadOptions);

    	// Set password to null
    	workbook.getSettings().setPassword(null);

    	// Save the decrypted Excel file
    	workbook.save("D:\\Pass.xlsx");
    }
 
}