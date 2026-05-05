package db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
// import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
// import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

	int top = 0;
	private Connection conn;

	public HashMap<Integer, Zamestnanec> DB;

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
	public int IDgen() {
		top++;
		return top;
	}

	public boolean OdebratZam(int IDz) {
		if (!DB.containsKey(IDz)) {
			System.out.println("Chyba: Zamestnanec s ID " + IDz + " neexistuje.");
			return false;
		}
		
		DB.remove(IDz);
		for (Zamestnanec z : DB.values()) {
			z.getListZam().remove(IDz);
		}

		return true;
		
	}

	public boolean PridatSpol(int IDz, int IDk, UrovSpol u) {

		if (!DB.containsKey(IDz)) {
			System.out.println("Chyba: Zamestnanec s ID " + IDz + " neexistuje.");
			return false;
		}

		if (!DB.containsKey(IDk)) {
			System.out.println("Chyba: Kolega s ID " + IDk + " neexistuje.");
			return false;
		}
		if (IDz == IDk) {
			System.out.println("Chyba: Zamestnanec nemuze evidovat spolupraci sam se sebou.");
			return false;
		}

		DB.get(IDz).getListZam().put(IDk, u);
		return true;
	}

	public boolean NajitZam(int IDz) {
		if (!DB.containsKey(IDz)) {
			System.out.println("Zamestnanec s ID " + IDz + " neexistuje.");
			return false;
		}

		Zamestnanec z = DB.get(IDz);

		System.out.println("\n--- Karta zamestnance ID: " + z.getID() + " ---");
		System.out.println("Jmeno: " + z.getJmeno() + " " + z.getPrijm());
		System.out.println("Rok naroz: " + z.getRokNaroz());
		System.out.println("Pozice: " + (z instanceof BezpSp ? "Bezpecnostni specialista" : "Datovy analytik"));


		if (z.getListZam().isEmpty()) {
			System.out.println("Spoluprace: Zatim neeviduje zadne kolegy.");
		} else {
			System.out.println("Pocet spolupracovniku: " + z.getListZam().size());

			float prumSpol = 0;
			for (UrovSpol u : z.getListZam().values()) {
				prumSpol += u.ordinal() + 1; 
			}
			prumSpol = prumSpol / z.getListZam().size();

			System.out.printf("Prumerna kvalita spoluprace: %.2f (1=nejhorsi, 3=nejlepsi)%n", prumSpol);
		}
		System.out.println("-----------------------------------");
		
		return true;
	}

	public void DovedZam(int IDz) {
		if (!DB.containsKey(IDz)) {
			System.out.println("Zamestnanec s ID " + IDz + " neexistuje.");
			return;
		}
		Zamestnanec z = DB.get(IDz);
		z.dovednost(DB);
	}

	public void VypisAbc() {

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
				pocetHodnoticich++; 
			}
		}

		if (idMaxSpol != -1 && maxSpol > 0) {
			System.out.println("\n--- Zamestnanec s nejvice vazbami (" + maxSpol + ") ---");
			NajitZam(idMaxSpol);
		} else {
			System.out.println("\nZatim nikdo neeviduje zadnou spolupraci.");
		}

		if (pocetHodnoticich > 0) {
			prumSpol = prumSpol / pocetHodnoticich;
			System.out.printf("Celkova prumerna kvalita spoluprace ve firme: %.2f%n", prumSpol);
		}
	}

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
	
		try (FileWriter fw = new FileWriter(jmenoSouboru);
				BufferedWriter bw = new BufferedWriter(fw)) {

			for (Zamestnanec z : DB.values()) {
				if (DB.isEmpty())
					break;

				bw.write(z.getClass().getSimpleName() + "," + z.getID() + "," + z.getJmeno() + "," + z.getPrijm() + ","
						+ z.getRokNaroz());
				bw.newLine();
			}

			bw.newLine();

			for (Zamestnanec z : DB.values()) {
				for (Entry<Integer, UrovSpol> k : z.getListZam().entrySet()) {
					bw.write(z.getID() + "," + k.getKey() + "," + k.getValue().ordinal());
					bw.newLine();
				}
			}
		} catch (IOException e) {
			System.out.println("Nepodarilo se otevrit soubor");
		}
	}

	public void NacistSoubor(String jmenoSouboru) {
		
		DB.clear();
		top = 0;
		
		try (BufferedReader br = new BufferedReader(new FileReader(jmenoSouboru))) {
			String radek;

			while ((radek = br.readLine()) != null) {
				if (radek.trim().isEmpty())
					continue;

				String[] casti = radek.split(",");

				try {
					switch (casti[0]) {
						case "DataAn":
							int idDA = Integer.parseInt(casti[1]);
							DB.put(idDA, new DataAn(idDA, casti[2], casti[3], Integer.parseInt(casti[4])));
							break;

						case "BezpSp":
							int idBS = Integer.parseInt(casti[1]);
							DB.put(idBS, new BezpSp(idBS, casti[2], casti[3], Integer.parseInt(casti[4])));
							break;

						default:
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

	public void ZapisSQL(String jmenoDB) {
		if (!connect(jmenoDB)) {
			System.out.println("K databazi se nebylo mozne pripojit");
			return;
		}

		try (Statement stmt = conn.createStatement()) {
			stmt.execute(
					"CREATE TABLE IF NOT EXISTS zamestnanci (jeDataAn INT, ID INT PRIMARY KEY, jmeno VARCHAR(50), prijm VARCHAR(50), rokNar INT)");
			stmt.execute("CREATE TABLE IF NOT EXISTS spoluprace (IDzam INT, IDkol INT, UrovSpol INT)");

			stmt.execute("DELETE FROM zamestnanci");
			stmt.execute("DELETE FROM spoluprace");

			String sqlZam = "INSERT INTO zamestnanci(jeDataAn, ID, jmeno, prijm, rokNar) VALUES(?,?,?,?,?)";
			try (PreparedStatement pstmtZam = conn.prepareStatement(sqlZam)) {
				for (Zamestnanec z : DB.values()) {
					pstmtZam.setInt(1, (z instanceof DataAn) ? 1 : 0);
					pstmtZam.setInt(2, z.getID());
					pstmtZam.setString(3, z.getJmeno());
					pstmtZam.setString(4, z.getPrijm());
					pstmtZam.setInt(5, z.getRokNaroz());
					pstmtZam.executeUpdate();
				}
			}

			String sqlSpol = "INSERT INTO spoluprace(IDzam, IDkol, UrovSpol) VALUES(?,?,?)";
			try (PreparedStatement pstmtSpol = conn.prepareStatement(sqlSpol)) {
				for (Zamestnanec z : DB.values()) {
					for (Map.Entry<Integer, UrovSpol> k : z.getListZam().entrySet()) {
						pstmtSpol.setInt(1, z.getID());
						pstmtSpol.setInt(2, k.getKey());
						pstmtSpol.setInt(3, k.getValue().ordinal());
						pstmtSpol.executeUpdate();
					}
				}
			}
			System.out.println("Zapis do SQL uspesne dokoncen.");

		} catch (SQLException e) {
			System.out.println("Chyba SQL: " + e.getMessage());
		} finally {
			disconnect();
		}
	}

	public void NacistSQL(String jmenoDB) {
		if (!connect(jmenoDB)) {
			System.out.println("K databazi se nebylo mozne pripojit");
			return;
		}

		DB.clear();
		top = 0;

		try (Statement stmt = conn.createStatement()) {
			ResultSet rsZam = stmt.executeQuery("SELECT * FROM zamestnanci");
			while (rsZam.next()) {
				int id = rsZam.getInt("ID");
				String jmeno = rsZam.getString("jmeno");
				String prijm = rsZam.getString("prijm");
				int rokNar = rsZam.getInt("rokNar");
				int jeDataAn = rsZam.getInt("jeDataAn");

				Zamestnanec z;
				if (jeDataAn == 1) {
					z = new DataAn(id, jmeno, prijm, rokNar);
				} else {
					z = new BezpSp(id, jmeno, prijm, rokNar);
				}
				DB.put(id, z);

				if (id > top)
					top = id;
			}

			ResultSet rsSpol = stmt.executeQuery("SELECT * FROM spoluprace");
			while (rsSpol.next()) {
				int idZ = rsSpol.getInt("IDzam");
				int idK = rsSpol.getInt("IDkol");
				int urov = rsSpol.getInt("UrovSpol");

				if (DB.containsKey(idZ)) {
					DB.get(idZ).getListZam().put(idK, UrovSpol.values()[urov]);
				}
			}
			System.out.println("Data z SQL uspesne nactena.");

		} catch (SQLException e) {
			System.out.println("Chyba SQL: " + e.getMessage());
		} finally {
			disconnect();
		}
	}

	public boolean connect(String dbCesta) {
		conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbCesta);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	public void disconnect() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

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
