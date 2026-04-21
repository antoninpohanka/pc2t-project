package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DB {
	
	int top=0;

	//struktura pro polozky databaze
	public HashMap<Integer, Zamestnanec> DB;
	
	//inicializace db
	public DB() {
		this.DB = new HashMap<Integer, Zamestnanec>();
	}
	
	
	public void PridatZam(String jmeno, String prijm, int rokNar, boolean jeDataAn) {
		
		int id = IDgen();
		
		if(jeDataAn) {	
			DB.put(id, new DataAn(id, jmeno, prijm, rokNar));
		}else {
			DB.put(id, new BezpSp(id, jmeno, prijm, rokNar));
		}
		
	}
	
	//pomocná metoda pro generovaní id, prozatím jen inkrementuje hodnotu id=1,2,3...
	public int IDgen() {
		top++;
		return top;
	}
	
	
	public void OdebratZam(int IDz) {
		//poslat vyjimky vys
		//osetrit - prazdna DB, zamestnanec neexistuje
		DB.remove(IDz);
		
		
		//odebere vazby
		for (Zamestnanec z : DB.values()) {
			z.getListZam().remove(IDz);
		}
		
	}
	
	public void PridatSpol(int IDz, int IDk, UrovSpol u) {
		DB.get(IDz).getListZam().put(IDk, u);
		//osetrit prazdna DB, zamestnanec neexistuje, kolega neexistuje
		
		//jak vnimat spoluprace? - kdyz se zada nova spoluprace s kolegou co uz ma praci evidovanou
		// prepise se na novou uroven nebo se zprumeruje - potom by se uroven spoluprace musela evidovat jako kombinace dvou parametru = prumer urovne spoluprace 1=dobra, 2=prumerna, 3=spatna a counter pocitajici pocet spolupraci co byly zadany
		
	}
	
	public void NajitZam(int IDz) {	
		System.out.println(DB.get(IDz).toString());
		
	}
	
	public void DovedZam(int IDz) {
		DB.get(IDz).dovednost(DB);
	}
	
	
	public void VypisAbc() {
		//ArrayList<String> serazene = new ArrayList<String>(DB.keySet());
		
		/*
		 * reseni ve stylu = iterovat pres databazi, pokud .getClass bude BezpSp priradit do jednoho al, dataAn do druheho al
		 * pote pouzit collection.?? metoda na serazeni prijmeni podle abecedy pro kazdy al
		 * 
		 * */

		
		
	}
	
	public void Stat() {
		//podobny algoritmus u bezpsp ale aplikovany na celou db
	}
	
	public void PocetZam() {
		//efektivnejsi - 2 countery, podle getClass pricist k jednomu nebo druhemu
		//pocitat pro obe skupiny nebo na zacatku zvolit pro kterou se bude pocitat?
	}
	
	public void ZapisSoubor() {
		//ukradnout ze cvik
	}
	
	public void NacistSoubor() {
		//ukradnout ze cvik
	}
	
	public void ZapisSQL() {
		//ukradnout ze cvik
		
		//potreba dve tabulky
		
		//prvni zamestnanci klic id, jmeno, prijmeni, roknaroz, skupina
		//druha spouprace klic id zamestnanec, id kolega, uroven spoluprace
		
		
	}
	
	public void NacistSQL() {
		//ukradnout ze cvik
	}
	
	//pomocna metoda, vypis databaze
	public void VypisDB() {
		for (Entry<Integer, Zamestnanec> entry : DB.entrySet()) {
			Integer key = entry.getKey();
			Zamestnanec val = entry.getValue();
			
			System.out.println(key +" "+val);
			
		}
	}
	
	
}
