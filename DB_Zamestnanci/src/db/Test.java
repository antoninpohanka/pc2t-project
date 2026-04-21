package db;


//třída pro testování jednotlivých komponentů
public class Test {

	public static void main(String[] args) {
	
		//inicializace DB
		DB db = new DB();
		
		//pridat zamestnance Bezp. specialista
		db.PridatZam("John", "Doe", 1964, false);
		//pridat zam Data analyst
		db.PridatZam("John", "Doe", 1964, true);
		
		db.VypisDB();
		
		//pridat spolupraci
		db.PridatSpol(1, 2, UrovSpol.DOBRÁ);
		
		db.VypisDB();
		
		//odebrani zamestnance
		db.OdebratZam(2);
		
		db.VypisDB();
		
		//BezpSp bs1 = new BezpSp(5, "John", "Doe", 1999);
		//System.out.println(bs1.getClass().getSimpleName());
		
		//pridat zamestnance Bezp. specialista
		db.PridatZam("John", "Newman", 1964, false);
				//pridat zam Data analyst
		db.PridatZam("John", "Doe", 1964, true);
		db.PridatZam("John", "DaVinci", 1964, true);
		
		db.VypisAbc();
		
		db.PocetZam();
		
	}
	
}
