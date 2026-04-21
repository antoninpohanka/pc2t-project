package db;

import java.util.HashMap;

public abstract class Zamestnanec {
	public int ID;
	public String jmeno;
	public String prijm;
	public int rokNaroz;
	
	public HashMap<Integer, UrovSpol> listZam = new HashMap<Integer, UrovSpol>();

	public Zamestnanec(int iD, String jmeno, String prijm, int rokNaroz) {
		super();
		this.ID = iD;
		this.jmeno = jmeno;
		this.prijm = prijm;
		this.rokNaroz = rokNaroz;
	}

	public String getJmeno() {
		return jmeno;
	}

	public void setJmeno(String jmeno) {
		this.jmeno = jmeno;
	}

	public String getPrijm() {
		return prijm;
	}

	public void setPrijm(String prijm) {
		this.prijm = prijm;
	}

	public int getRokNaroz() {
		return rokNaroz;
	}

	public void setRokNaroz(int rokNaroz) {
		this.rokNaroz = rokNaroz;
	}

	public HashMap<Integer, UrovSpol> getListZam() {
		return listZam;
	}

	public void setListZam(HashMap<Integer, UrovSpol> listZam) {
		this.listZam = listZam;
	}
 
	
	public abstract void dovednost(HashMap<Integer, Zamestnanec> db);

	public abstract void dovednost();
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	@Override
	public String toString() {
		return "Zamestnanec [ID=" + ID + ", jmeno=" + jmeno + ", prijm=" + prijm + ", rokNaroz=" + rokNaroz
				+ ", listZam=" + listZam + "]";
	}
	
	
	
	
	
	
}
