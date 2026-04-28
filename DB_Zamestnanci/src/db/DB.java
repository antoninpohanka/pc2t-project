package db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

	int top = 0;
	private Connection conn;

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

	public void ZapisSoubor(String jmenoSouboru) {
		// ukradnout ze cvik
		try (FileWriter fw=new FileWriter(jmenoSouboru);
				BufferedWriter bw=new BufferedWriter(fw))
			{
				//bw.write("Pocet: "+DB.size());
				//bw.newLine();
				for (Zamestnanec z : DB.values()) {
					if (DB.isEmpty())
						break;
					
					bw.write(z.getClass().getSimpleName() +"," + z.getID() + "," + z.getJmeno() + "," + z.getPrijm() + "," + z.getRokNaroz());
					bw.newLine();
				}
				
				bw.newLine();
				
				for (Zamestnanec z : DB.values()) {
				for (Entry<Integer, UrovSpol> k : z.getListZam().entrySet()) {
					
					bw.write(z.getID() + "," + k.getKey() + "," + k.getValue().ordinal());
					bw.newLine();
				}
				}
			} 
			catch (IOException e) {
				System.out.println("Nepodarilo se otevrit soubor");
			}
	}

	//vibecoded
	public void NacistSoubor(String jmenoSouboru) {
		try (BufferedReader br = new BufferedReader(new FileReader(jmenoSouboru))) {
	        String radek;
	        
	        while ((radek = br.readLine()) != null) {
	            // Přeskočíme prázdné řádky, aby program nespadl
	            if (radek.trim().isEmpty()) continue;

	            // Rozsekáme řádek podle čárky
	            String[] casti = radek.split(",");

	            try {
	                // Rozhodujeme podle prvního prvku v poli
	                switch (casti[0]) {
	                    case "DataAn":
	                        int idDA = Integer.parseInt(casti[1]);
	                        DB.put(idDA, new DataAn(idDA, casti[2], casti[3], Integer.parseInt(casti[4])));
	                        //PridatZam(casti[2], casti[3], Integer.parseInt(casti[4]), true);
	                        break;

	                    case "BezpSp":
	                        int idBS = Integer.parseInt(casti[1]);
	                        DB.put(idBS, new BezpSp(idBS, casti[2], casti[3], Integer.parseInt(casti[4])));
	                        //PridatZam(casti[2], casti[3], Integer.parseInt(casti[4]), false);
	                        break;

	                    default:
	                        // Pokud to není textový kód, zkusíme, jestli jde o číselnou vazbu (např. 1,2,2)
	                        if (casti.length >= 3) {
	                            int odId = Integer.parseInt(casti[0]);
	                            int doId = Integer.parseInt(casti[1]);
	                            int uroven = Integer.parseInt(casti[2]);
	                            PridatSpol(odId, doId, UrovSpol.values()[uroven]);
	                        }
	                        break;
	                }
	            } catch (NumberFormatException e) {
	                System.out.println("Chyba formátu čísla na řádku: " + radek);
	            } catch (Exception e) {
	                System.out.println("Chyba při zpracování řádku: " + e.getMessage());
	            }
	        }
	    } catch (IOException e) {
	        System.out.println("Soubor nelze otevřít nebo číst: " + e.getMessage());
	    }
	}

	
    

// potreba dve tabulky

// prvni zamestnanci klic id, jmeno, prijmeni, roknaroz, skupina
// druha spouprace klic id zamestnanec, id kolega, uroven spoluprace
	public void ZapisSQL(String jmenoDB) {
		
		if (!connect(jmenoDB))
	    { 
		   	//Class.forName("org.sqlite.JDBC"); - mozne reseni pokud nefunguje i s pridanymi JARs
	    	System.out.println("K databázi se nebylo možné připojit");
	    	return;
	    }
	   
	   System.out.println("pripojeno");

	   //DOPLNIT - zkontrolovat, jestli db ma tabulku zamestnanci, pokud ne vytvorit novou??  --- asi si handluje samo sql
	   //jak resit pokud se budou nahravat zamestnanci co maji id spolecne s nejakym s tim v db???
  	
	   //sqlite nema boolean - bere int 1 jako true a int 0 jako false
	   String sqlTabulka = "CREATE TABLE IF NOT EXISTS zamestnanci(" +
				"jeDataAn INT NOT NULL," +
                "id INT PRIMARY KEY," +
                "jmeno VARCHAR(50) NOT NULL," +
                "prijm VARCHAR(50) NOT NULL," +
                "rokNar INT NOT NULL)";

	        try {
	        	Statement pridatTab = conn.createStatement();
				pridatTab.executeUpdate(sqlTabulka);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		    String sql = "INSERT INTO zamestnanci(jeDataAn, ID, jmeno, prijm, rokNar) VALUES(?,?,?,?,?)";
 
		       //zapis zamestnancu
		      for (Zamestnanec z : DB.values()) {
		    	  
		    	   try {
		               PreparedStatement pstmt = conn.prepareStatement(sql); 
		               pstmt.setInt(1, (z.getClass().getSimpleName().equals("DataAn") ? 1:0));
		               pstmt.setInt(2, z.getID());
		               pstmt.setString(3, z.getJmeno());
		               pstmt.setString(4, z.getPrijm());
		               pstmt.setInt(5, z.getRokNaroz());
		               pstmt.executeUpdate();
		           } 
		            catch (SQLException e) {
		                System.out.println(e.getMessage());
		           }
		    	  
		    	  }
		    	  
			    //udelat neco podobnyho ale pro druhou tabulku - spoluprace 
			    // 1:N??, struktura IDz, IDk, UrovSpol ale na INT 

		     
		      String sqlTabulka2 = "CREATE TABLE IF NOT EXISTS spoluprace(" +
						"IDzam INT NOT NULL," +
		                "IDkol INT NOT NULL," +
		                "UrovSpol INT NOT NULL)";

			        try {
			        	Statement pridatTab = conn.createStatement();
						pridatTab.executeUpdate(sqlTabulka2);
					} catch (SQLException e) {
						e.printStackTrace();
					}
			        
			  String sql2 = "INSERT INTO spoluprace(IDzam, IDkol, UrovSpol) VALUES(?,?,?)";

		      for (Zamestnanec z : DB.values()) {
		    	  for (Entry<Integer, UrovSpol> k : z.getListZam().entrySet()) {
		    	   try {
		               PreparedStatement pstmt = conn.prepareStatement(sql2); 
		               pstmt.setInt(1, z.getID());
		               pstmt.setInt(2, k.getKey());
		               pstmt.setInt(3, k.getValue().ordinal()+1);
		               pstmt.executeUpdate();
		           	}
		            catch (SQLException e) {
		                System.out.println(e.getMessage());
		           }
		    	  }
		      }
		      
		      System.out.println("KONEC ZVONEC");
		      
		     disconnect();     
		  	}

	public void NacistSQL() {
		// ukradnout ze cvik
		//vymazat databazi ???
		
		//PRIDAT ohlidani pripojeni + odpojeni od db
		//connect("jdbc:sqlite:C:\\dbDemo\\demodb.db");	
		
		
		
		
		
		/*String sql = "SELECT * FROM user";
        try {
             Statement stmt  = con.createStatement();
             ResultSet rs    = stmt.executeQuery(sql);
             while (rs.next()) {

            	 if(rs.getInt("jeDataAn")==1 ? true : false) {
            		 DB.put(rs.getInt("id_user"), new DataAn(rs.getInt("id_user"), rs.getString("jmeno"), rs.getString("prijm"), rs.getInt("rokNar")));
            	 }else {
            		 DB.put(rs.getInt("id_user"), new BezpSp(rs.getInt("id_user"), rs.getString("jmeno"), rs.getString("prijm"), rs.getInt("rokNar")));
            	 }		
            }
        } 
        catch (SQLException e) {
             System.out.println(e.getMessage());
        }*/
		
		//pridat odpojeni
		
		
	}
	
	
	//UKRADNUTO ZE CVIK pomocna metoda - pripojeni k db
	public boolean connect(String dbCesta) { 
	       conn= null; 
	       try {
	              conn = DriverManager.getConnection("jdbc:sqlite:"+dbCesta);                       
	       } 
	      catch (SQLException e) { 
	            System.out.println(e.getMessage());
		    return false;
	      }
	      return true;
	}
	
	
	//UKRADENO ZE CVIK - pomocna metoda - odpojeni od db
	public void disconnect() { 
		if (conn != null) {
			try { 
				conn.close();  
			} 
			catch (SQLException e) { 
			   System.out.println(e.getMessage()); 
			}
		}
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
