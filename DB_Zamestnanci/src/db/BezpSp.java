package db;

import java.util.HashMap;


public class BezpSp extends Zamestnanec {

	public BezpSp(int iD, String jmeno, String prijm, int rokNaroz) {
		super(iD, jmeno, prijm, rokNaroz);

	}

	@Override
	public String toString() {
		return String.format("[ID: %d] %s %s (nar. %d) - Bezpecnostni specialista", ID, jmeno, prijm, rokNaroz);
	}

	@Override
	public void dovednost(HashMap<Integer, Zamestnanec> db) {
		System.out.println("Tento zamestnanec nema pristup k teto metode");
	}

	@Override
	public void dovednost(HashMap<Integer, Zamestnanec> db, int IDkol) {
		
		float spolUrov = 0;
		int spolPoc = 0;
		float celkSpol = 0;
		int celkPoc = 0;
		
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
			celkPoc++;
		}
		
		if(spolPoc==0) {
			System.out.println("Zamestnanec nema zadne spoluprace, riziko nelze vyhodnotit");
		}else {
			prumSpol = 10 - (((spolUrov/spolPoc)/(celkSpol/celkPoc)) * (10 / 3));

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

