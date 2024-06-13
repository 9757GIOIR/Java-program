package Checkfile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;


public class DB {
   public static void main( String args[] ) {
      Connection c = null;
      Statement stmt = null;
      try {
         Class.forName("org.postgresql.Driver");
         c = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/Chatbot_IC",
            "postgres", "Balaji_97");
         c.setAutoCommit(false);
         System.out.println("Opened database successfully");

         stmt = c.createStatement();
         ResultSet rs = stmt.executeQuery( "SELECT * FROM ic_chatbot_details;" );
         while ( rs.next() ) {
        	 
            String state = rs.getString("workflow_payload");
            System.out.println( "workflow payload = " + state );
            String arr=state.toString();
            JSONArray jsonArr = new JSONArray(arr);

            for (int i = 0; i < jsonArr.length(); i++)
            {
            	JSONObject object = jsonArr.getJSONObject(i);  
            	System.out.println(object.getString("orgCode"));  
            	System.out.println(object.getString("userId"));  
            System.out.println();
         }
         }
         rs.close();
         stmt.close();
         c.close();
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
         System.exit(0);
      }
      System.out.println("Operation done successfully");
   }
}
 

 
