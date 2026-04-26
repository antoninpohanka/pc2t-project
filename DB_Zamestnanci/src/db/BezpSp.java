package db;

import java.util.HashMap;

public class BezpSp extends Zamestnanec {

	public BezpSp(int iD, String jmeno, String prijm, int rokNaroz) {
		super(iD, jmeno, prijm, rokNaroz);

	}

	@Override
	public void dovednost() {
		// nepotrebuje parametr,
		// podobny algoritmus u bezpsp ale aplikovany na celou db
		float prumSpol = 0;

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

	}

	@Override
	public String toString() {
		return String.format("[ID: %d] %s %s (nar. %d) - Bezpečnostní specialista", ID, jmeno, prijm, rokNaroz);
	}

	@Override
	public void dovednost(HashMap<Integer, Zamestnanec> db) {
		// System.out.println("uzivatel nema tuto dovednost");
		// teoreticky muzeme zajistit, aby se uziv. na tuhle nikdy nedostal

	}

}
