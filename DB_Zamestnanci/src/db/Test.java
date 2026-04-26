package db;

import java.util.Iterator;
import java.util.Map.Entry;

public class Test {

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
        
        
        db.ZapisSoubor("test1.txt");
		
        System.out.println("zapis");
        
        db.DB.clear();
        
        System.out.println("clear");
        
        db.NacistSoubor("test1.txt");
		
        System.out.println("end");
        
        db.VypisDB();
        
        System.out.println(db.DB.containsKey(1));
        System.out.println(db.DB.containsKey(2));
        System.out.println(db.DB.containsKey(3));
        
        for (Zamestnanec z : db.DB.values()) {
        	for (Entry<Integer, UrovSpol> k : z.getListZam().entrySet()) {
        		System.out.println(k.getKey() + ", " + k.getValue());
        		
        	}
		}
        
        
	}

}