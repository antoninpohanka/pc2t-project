package db;

import java.util.HashMap;
import java.util.Set;

public class DataAn extends Zamestnanec {

	public DataAn(int iD, String jmeno, String prijm, int rokNaroz) {
		super(iD, jmeno, prijm, rokNaroz);
		// this.listZam = null;
	}

	@Override
	public void dovednost(HashMap<Integer, Zamestnanec> db) {

		Zamestnanec kolega = null;
		int spolMax = 0;
		Set<Integer> mojeSpol = this.getListZam().keySet();

		// iterace pres celou databazi
		for (Zamestnanec z : db.values()) {

			// preskakuje instanci, pro kterou je spousten (aby nehledal sam u sebe)
			if (z.getID() == this.ID) {
				continue;
			}

			int spol = 0;

			for (Integer ID : z.getListZam().keySet()) {
				if (mojeSpol.contains(ID)) {
					spol++;
				}
			}

			if (spol > spolMax) {
				spolMax = spol;
				kolega = z;
			}
		}

		if (kolega != null) {
			System.out.println("Nejvice spolecnych spolupracovniku ma kolega:\n" + kolega);
		} else {
			System.out.println("Zatim nebyl nalezen zadny spolecny spolupracovnik v databazi.");
		}
	}

	@Override
	public String toString() {
		return String.format("[ID: %d] %s %s (nar. %d) - Datovy analytik", ID, jmeno, prijm, rokNaroz);
	}

	@Override
	public void dovednost() {
		// netusim
	}

}
