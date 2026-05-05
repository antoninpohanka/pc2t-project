package db;

import java.util.HashMap;
import java.util.Scanner;


public class BezpSp extends Zamestnanec {

	public BezpSp(int iD, String jmeno, String prijm, int rokNaroz) {
		super(iD, jmeno, prijm, rokNaroz);

	}

	/*@Override
	public void dovednost() {
		// nepotrebuje parametr,
		// podobny algoritmus u bezpsp ale aplikovany na celou db
		/*float prumSpol = 0;

		// mozna misto tohodle pouzit metodu z DB co pocita prumernou spolupraci?
		for (UrovSpol u : this.getListZam().values()) {
			prumSpol += u.ordinal() + 1;
		}

		prumSpol = prumSpol / this.getListZam().size();

		System.out.println("Prumerna spoluprace je: " + prumSpol);

		// definujeme si napr. rizik skore - 1 az 10

		prumSpol = 10 - (prumSpol * (10 / 3));

		String riziko;

		if (prumSpol < 4) {
			riziko = "nizke";
		} else if (prumSpol >= 4 || prumSpol <= 6) {
			riziko = "stredni";
		} else {
			riziko = "vysoke";
		}

		System.out.println("Riziko spoluprace je: " + prumSpol + " - " + riziko);
		
	} */

	@Override
	public String toString() {
		return String.format("[ID: %d] %s %s (nar. %d) - Bezpecnostni specialista", ID, jmeno, prijm, rokNaroz);
	}

	@Override
	public void dovednost(HashMap<Integer, Zamestnanec> db) {
		
		Scanner sc1 = new Scanner(System.in);;
		
		int IDkol =0;
		
		int spolUrov = 0;
		int spolPoc = 0;
		int celkSpol =0;
		
		boolean loop = true;
		
		while (loop) {
    		try {
    			System.out.println("Zadejte cislo kolegy: ");
    			String vstup = sc1.nextLine();
    			int cislo=Integer.parseInt(vstup);
    			if(cislo<1 || cislo>db.size()) {
    				System.out.println("Chyba: Cislo musi byt mezi 0" + " - " + db.size());
    			}else {	
    				IDkol = cislo;
    				loop = false;
    			}	
    		} catch (NumberFormatException e) {
    			System.out.println("Nebylo zadano cele cislo");
    	    } 
			
		}
		
		if(!db.containsKey(IDkol)) {
			return;
		}
		
		
		for (Zamestnanec z : db.values()) {			
			if(z.getListZam().containsKey(IDkol)) {
				spolUrov+=z.getListZam().get(IDkol).ordinal()+1;
				spolPoc++;
			}
			
			for (UrovSpol u : z.getListZam().values()) {
				celkSpol += u.ordinal()+1;
			}
			
		}
		
		float prumSpol = 0;
		for (UrovSpol u : this.getListZam().values()) {
			prumSpol += u.ordinal() + 1;
		}
		
		if(spolPoc==0) {
			System.out.println("Zamestnanec nema zadne spoluprace, riziko nelze vyhodnotit");
		}else {
			prumSpol = 10 - (((spolUrov/spolPoc)/(celkSpol/db.size())) * (10 / 3));

		String riziko;

		if (prumSpol < 4) {
			riziko = "nizke";
		} else if (prumSpol >= 4 && prumSpol <= 6) {
			riziko = "stredni";
		} else {
			riziko = "vysoke";
		}

		System.out.println("Riziko spoluprace je: " + prumSpol + " - " + riziko);
		}
		

	}
		
	
}

