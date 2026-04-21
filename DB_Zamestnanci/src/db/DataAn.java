package db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class DataAn extends Zamestnanec{

	public DataAn(int iD, String jmeno, String prijm, int rokNaroz) {
		super(iD, jmeno, prijm, rokNaroz);
		this.listZam = null;
	}
	
	

	@Override
	public void dovednost(HashMap<Integer, Zamestnanec> db) {
		
		//zamestnanec, co ma nejvice stejnych keys
		Zamestnanec kolega = null;
		int spolMax = 0;
		
		Set<Integer>mojeSpol = this.getListZam().keySet();
		
		for (Entry<Integer, Zamestnanec> entry : db.entrySet()) {
			
			//iterace pres celou databazi
			for (Zamestnanec z : db.values()) {
				
				//preskakuje instanci, pro kterou je spousten
				if(z.getID() == this.ID) {
					continue;
				}
				
				int spol = 0;
				
				for (Integer ID : z.getListZam().keySet()) {
					if(mojeSpol.contains(ID)) {
						spol++;
					}
				}
				
				if(spol > spolMax) {
					spolMax = spol;
					kolega = z;
				}
				
			}
		}
		
		System.out.println(kolega);
		
	}



	@Override
	public String toString() {
		return "DataAn [ID=" + ID + ", jmeno=" + jmeno + ", prijm=" + prijm + ", rokNaroz=" + rokNaroz + ", listZam="
				+ listZam + "]";
	}



	@Override
	public void dovednost() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
