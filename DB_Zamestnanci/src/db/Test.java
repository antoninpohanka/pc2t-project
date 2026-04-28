package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map.Entry;

//smazat tuhle tridu na konec
public class Test {

	private Connection conn;
	
    public static void main(String[] args) {
        //ObsluhaDB obsluha = new ObsluhaDB();
        //obsluha.spustitMenu();

        DB db = new DB();

         // Pro testovani
         db.PridatZam("Jan", "Novak", 1980, false); // Bezp. spec. (ID 1)
         db.PridatZam("Petr", "Svoboda", 1992, true); // Dat. analytik (ID 2)
         db.PridatZam("Eva", "Nova", 1985, true); // Dat. analytik (ID 3)
         db.PridatSpol(1, 2, UrovSpol.DOBRA);
         db.PridatSpol(1, 3, UrovSpol.PRUMERNA);
         
         System.out.println("zam v db");

        // db.ZapisSoubor("test1.txt");

        // System.out.println("zapis");

        // db.DB.clear();

        // System.out.println("clear");

        // db.NacistSoubor("test1.txt");

        // System.out.println("end");

        // db.VypisDB();

        // System.out.println(db.DB.containsKey(1));
        // System.out.println(db.DB.containsKey(2));
        // System.out.println(db.DB.containsKey(3));

        // for (Zamestnanec z : db.DB.values()) {
        // for (Entry<Integer, UrovSpol> k : z.getListZam().entrySet()) {
        // System.out.println(k.getKey() + ", " + k.getValue());

        // }
        // }
        
        
        
  /*     
        if (!db.connect("C:\\dbDemo\\demodb.db"))
	    { 
		   	//Class.forName("org.sqlite.JDBC");
	    	System.out.println("K databázi se nebylo možné připojit");
	    	return;
	    }

	   
	   System.out.println("pripojeno");
		   
		   
		   db.disconnect();
*/
         db.ZapisSQL("C:\\dbDemo\\demodb.db");
         
         
    }

}