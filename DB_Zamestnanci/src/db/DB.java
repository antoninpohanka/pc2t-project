package db;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DB {

	int top = 0;

	// struktura pro polozky databaze
	public HashMap<Integer, Zamestnanec> DB;

	// inicializace db
	public DB() {
		this.DB = new HashMap<Integer, Zamestnanec>();
	}

	public void PridatZam(String jmeno, String prijm, int rokNar, boolean jeDataAn) {

		int id = IDgen();

		if (jeDataAn) {
			DB.put(id, new DataAn(id, jmeno, prijm, rokNar));
		} else {
			DB.put(id, new BezpSp(id, jmeno, prijm, rokNar));
		}

	}

	// pomocná metoda pro generovaní id, prozatím jen inkrementuje hodnotu
	// id=1,2,3...
	public int IDgen() {
		top++;
		return top;
	}

	public void OdebratZam(int IDz) {
		// poslat vyjimky vys
		// osetrit - prazdna DB, zamestnanec neexistuje
		DB.remove(IDz);

		// odebere vazby
		for (Zamestnanec z : DB.values()) {
			z.getListZam().remove(IDz);
		}

	}

	public boolean PridatSpol(int IDz, int IDk, UrovSpol u) {
		// DB.get(IDz).getListZam().put(IDk, u);
		// osetrit prazdna DB, zamestnanec neexistuje, kolega neexistuje

		// jak vnimat spoluprace? - kdyz se zada nova spoluprace s kolegou co uz ma
		// praci evidovanou

		// prepise se na novou uroven nebo se zprumeruje - potom by se uroven spoluprace
		// musela evidovat jako kombinace dvou parametru = prumer urovne spoluprace
		// 1=dobra, 2=prumerna, 3=spatna a counter pocitajici pocet spolupraci co byly
		// zadany

		// bezpecak bere vsechny spoluprace a pokud mas spatny prumer, tak jsi riziko;
		// analytik hleda spolecne zname

		if (!DB.containsKey(IDz)) {
			System.out.println("Chyba: Zamestnanec s ID " + IDz + " neexistuje.");
			return false;
		}

		if (!DB.containsKey(IDk)) {
			System.out.println("Chyba: Kolega s ID " + IDk + " neexistuje.");
			return false;
		}

		// kontrola, jestli si nehodnoti sam sebe
		if (IDz == IDk) {
			System.out.println("Chyba: Zamestnanec nemuze evidovat spolupraci sam se sebou.");
			return false;
		}

		DB.get(IDz).getListZam().put(IDk, u);
		return true;
	}

	public void NajitZam(int IDz) {
		// osetreni, jestli vubec existuje zamestnanec
		if (!DB.containsKey(IDz)) {
			System.out.println("Zamestnanec s ID " + IDz + " neexistuje.");
			return;
		}

		Zamestnanec z = DB.get(IDz);

		System.out.println("\n--- Karta zamestnance ID: " + z.getID() + " ---");
		System.out.println("Jmeno: " + z.getJmeno() + " " + z.getPrijm());
		System.out.println("Rok naroz: " + z.getRokNaroz());
		System.out.println("Pozice: " + (z instanceof BezpSp ? "Bezpecnostni specialista" : "Datovy analytik"));

		// Statistiky spolupráce
		//
		if (z.getListZam().isEmpty()) {
			System.out.println("Spoluprace: Zatim neeviduje zadne kolegy.");
		} else {
			System.out.println("Pocet spolupracovniku: " + z.getListZam().size());

			float prumSpol = 0;
			for (UrovSpol u : z.getListZam().values()) {
				prumSpol += u.ordinal() + 1; // SPATNA=1, PRUMERNA=2, DOBRA=3
			}
			prumSpol = prumSpol / z.getListZam().size();

			// Vypise prumer zaokrouhleny na 2 desetinna mista
			System.out.printf("Prumerna kvalita spoluprace: %.2f (1=nejhorsi, 3=nejlepsi)%n", prumSpol);
		}
		System.out.println("-----------------------------------");
	}

	public void DovedZam(int IDz) {
		if (!DB.containsKey(IDz)) {
			System.out.println("Zamestnanec s ID " + IDz + " neexistuje.");
			return;
		}

		Zamestnanec z = DB.get(IDz);
		// analytik potrebuje k dovednosti celou databazi
		if (z instanceof DataAn) {
			z.dovednost(DB);
		} else if (z instanceof BezpSp) {
			z.dovednost();
		} else {
			System.out.println("Nastala chyba v aplikaci");
		}
	}

	public void VypisAbc() {
		// ArrayList<String> serazene = new ArrayList<String>(DB.keySet());

		/*
		 * reseni ve stylu = iterovat pres databazi, pokud .getClass bude BezpSp
		 * priradit do jednoho al, dataAn do druheho al
		 * pote pouzit collection.?? metoda na serazeni prijmeni podle abecedy pro kazdy
		 * al
		 * 
		 */

		ArrayList<Zamestnanec> BS = new ArrayList<Zamestnanec>();
		ArrayList<Zamestnanec> DA = new ArrayList<Zamestnanec>();

		for (Zamestnanec z : DB.values()) {

			if (z instanceof BezpSp) {
				BS.add(z);
			} else {
				DA.add(z);
			}
		}

		BS.sort(Comparator.comparing(Zamestnanec::getPrijm));
		DA.sort(Comparator.comparing(Zamestnanec::getPrijm));

		System.out.println("\n=== Abecedni vypis podle skupin ===");
		System.out.println("\n--- Bezpecnostni specialiste ---");
		if (BS.isEmpty())
			System.out.println("Zadni zamestnanci v teto skupine.");
		for (Zamestnanec z : BS)
			System.out.println(z);

		System.out.println("\n--- Datovi analytici ---");
		if (DA.isEmpty())
			System.out.println("Zadni zamestnanci v teto skupine.");
		for (Zamestnanec z : DA)
			System.out.println(z);
		System.out.println("===================================");

	}

	public void Stat() {
		if (DB.isEmpty()) {
			System.out.println("Databaze je zatim uplne prazdna.");
			return;
		}

		int maxSpol = 0;
		int idMaxSpol = -1;
		float prumSpol = 0;
		int pocetHodnoticich = 0;

		for (Zamestnanec z : DB.values()) {

			if (z.getListZam().size() > maxSpol) {
				maxSpol = z.getListZam().size();
				idMaxSpol = z.getID();
			}

			if (z.getListZam().size() > 0) {
				float prumSpolZam = 0;
				for (UrovSpol u : z.getListZam().values()) {
					prumSpolZam += u.ordinal() + 1;
				}
				prumSpolZam = prumSpolZam / z.getListZam().size();
				prumSpol += prumSpolZam;
				pocetHodnoticich++; // zjistit, kolik lidi realne nekoho hodnotilo
			}
		}

		// vypis zamestnance s nejvetsim poctem spolupraci
		if (idMaxSpol != -1 && maxSpol > 0) {
			System.out.println("\n--- Zamestnanec s nejvice vazbami (" + maxSpol + ") ---");
			NajitZam(idMaxSpol);
		} else {
			System.out.println("\nZatim nikdo neeviduje zadnou spolupraci.");
		}

		// vypis celkoveho prumeru
		if (pocetHodnoticich > 0) {
			prumSpol = prumSpol / pocetHodnoticich;
			System.out.printf("Celkova prumerna kvalita spoluprace ve firme: %.2f%n", prumSpol);
		}
	}

	// 1 = nejhorsi spoluprace, 3 nejlepsi

	public void PocetZam() {
		int bsPocet = 0;
		int daPocet = 0;

		for (Zamestnanec z : DB.values()) {
			if (z instanceof BezpSp)
				bsPocet++;
			else
				daPocet++;
		}

		System.out.println("\n--- Prehled zamestnancu ---");
		System.out.println("Bezpecnostni specialiste: " + bsPocet);
		System.out.println("Datovi analytici:         " + daPocet);
		System.out.println("Celkem v databazi:        " + (bsPocet + daPocet));
		System.out.println("---------------------------");
	}

	public void ZapisSoubor() {
		// ukradnout ze cvik
	}

	public void NacistSoubor() {
		// ukradnout ze cvik
	}

	public void ZapisSQL() {
		// ukradnout ze cvik

		// potreba dve tabulky

		// prvni zamestnanci klic id, jmeno, prijmeni, roknaroz, skupina
		// druha spouprace klic id zamestnanec, id kolega, uroven spoluprace

	}

	public void NacistSQL() {
		// ukradnout ze cvik
	}

	// pomocna metoda, vypis databaze
	public void VypisDB() {
		System.out.println("\n=== Kompletni vypis databaze ===");
		if (DB.isEmpty()) {
			System.out.println("Databaze je prazdna.");
		} else {
			for (Zamestnanec z : DB.values()) {
				System.out.println(z);
			}
		}
		System.out.println("================================");
	}

}
