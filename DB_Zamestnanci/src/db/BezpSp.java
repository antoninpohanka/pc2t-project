package db;

import java.util.HashMap;

public class BezpSp extends Zamestnanec{

	public BezpSp(int iD, String jmeno, String prijm, int rokNaroz) {
		super(iD, jmeno, prijm, rokNaroz);
		
	}

	@Override
	public void dovednost() {
	//nepotrebuje parametr, 
		
	}

	@Override
	public String toString() {
		return "BezpSp [ID=" + ID + ", jmeno=" + jmeno + ", prijm=" + prijm + ", rokNaroz=" + rokNaroz + ", listZam="
				+ listZam + "]";
	}

	@Override
	public void dovednost(HashMap<Integer, Zamestnanec> db) {
		//System.out.println("uzivatel nema tuto dovednost");
		//teoreticky muzeme zajistit, aby se uziv. na tuhle nikdy nedostal
	}
	
	

}
