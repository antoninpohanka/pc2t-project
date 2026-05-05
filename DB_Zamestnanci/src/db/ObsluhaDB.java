package db;

import java.util.Scanner;

public class ObsluhaDB {

    private DB db;

    public ObsluhaDB() {
        this.db = new DB();

    }

    public void spustitMenu() {
        Scanner sc = new Scanner(System.in);
        boolean bezi = true;

        System.out.println("Vitejte v Databazovem systemu zamestnancu!");

        while (bezi) {
            System.out.println("\n===== HLAVNI MENU =====");
            System.out.println("1) Pridat zamestnance");
            System.out.println("2) Pridat spolupraci");
            System.out.println("3) Odebrat zamestnance");
            System.out.println("4) Vyhledat zamestnance (dle ID)");
            System.out.println("5) Spustit dovednost zamestnance (dle ID)");
            System.out.println("6) Abecedni vypis zamestnancu");
            System.out.println("7) Vypsat statistiky (zamestnanec s nejvice vazbami)");
            System.out.println("8) Vypis poctu zamestnancu ve skupinach");
            System.out.println("9) Zobrazit celou databazi");
            System.out.println("10) Ulozit data do textoveho souboru");
            System.out.println("11) Nacist data z textoveho souboru");
            System.out.println("12) Ulozit data do SQL databaze");
            System.out.println("13) Nacist data z SQL databaze");
            System.out.println("0) Konec (Zavrit program)");
            System.out.print("Vase volba: ");

            String vstup = sc.nextLine();

            switch (vstup) {
                case "1":
                    System.out.print("Jmeno: ");
                    String jmeno = sc.nextLine();
                    System.out.print("Prijmeni: ");
                    String prijmeni = sc.nextLine();
                    System.out.print("Rok narozeni: ");
                    int rok = zkusNacistInt(sc, 1940, 2010);
                    System.out.print("Je datovy analytik? (ano/ne): ");
                    boolean jeDa = sc.nextLine().trim().equalsIgnoreCase("ano");
                    db.PridatZam(jmeno, prijmeni, rok, jeDa);
                    System.out.println("Zamestnanec uspesne pridan.");
                    break;
                case "2":
                    System.out.print("Zadejte vase ID: ");
                    int idZ = zkusNacistInt(sc, 1, Integer.MAX_VALUE);
                    System.out.print("Zadejte ID kolegy: ");
                    int idK = zkusNacistInt(sc, 1, Integer.MAX_VALUE);
                    if (idZ == idK) {
                        System.out.println("Chyba: Zamestnanec nemuze spolupracovat sam se sebou.");
                        break; 
                    }
                    
                    System.out.println("Vyberte uroven spoluprace (ciselne): ");
                    System.out.println("(1) Spatna ");
                    System.out.println("(2) Prumerna ");
                    System.out.println("(3) Dobra ");
      
                    int uroven = zkusNacistInt(sc, 1, 3);
                    
                    if(db.PridatSpol(idZ, idK, UrovSpol.values()[uroven-1])) {
                    	System.out.println("Spoluprace byla pridana");
                    }
                    
                    break;
                case "3":
                    System.out.print("Zadejte ID k odebrani: ");
                    int idO = zkusNacistInt(sc, 1, Integer.MAX_VALUE);
                    if(db.OdebratZam(idO)) {
                    	System.out.println("Pokus o smazani probehl.");
                    } 
                    break;
                case "4":
                    System.out.print("Zadejte ID zamestnance k vyhledani: ");
                    int idV = zkusNacistInt(sc, 1, Integer.MAX_VALUE);
                    db.NajitZam(idV);
                    break;
                case "5":
                    System.out.print("Zadejte ID zamestnance pro spusteni dovednosti: ");
                    int idD = zkusNacistInt(sc, 1, Integer.MAX_VALUE);
                    Zamestnanec z = db.DB.get(idD);
                    if (z instanceof DataAn) {
            			z.dovednost(db.DB);
            		} else if (z instanceof BezpSp) {
            			System.out.print("Zadejte ID kolegy pro zjisteni rizika spoluprace: ");
            			int idkol = zkusNacistInt(sc, 1, Integer.MAX_VALUE);
            			z.dovednost(db.DB, idkol);
            		} else {
            			System.out.println("Nastala chyba v aplikaci");
            		}
                    break;
                case "6":
                    db.VypisAbc();
                    break;
                case "7":
                    db.Stat();
                    break;
                case "8":
                    db.PocetZam();
                    break;
                case "9":
                    db.VypisDB();
                    break;
                case "10":
                    System.out.print("Zadejte nazev souboru pro ulozeni (napr. data.txt): ");
                    String souborZapis = sc.nextLine();
                    db.ZapisSoubor(souborZapis);
                    System.out.println("Zapis do souboru ukoncen.");
                    break;
                case "11":
                	System.out.print("Touto akci se smazou aktualni zaznamy v DB. Chcete pokracovat? (ano/ne): ");
                    boolean stop = sc.nextLine().trim().equalsIgnoreCase("ano");
                    if(!stop) {
                    	System.out.println("Navrat do menu");
                    	break;
                    }
                    System.out.print("Zadejte nazev souboru k nacteni (napr. data.txt): ");
                    String souborNacist = sc.nextLine();
                    db.NacistSoubor(souborNacist);
                    System.out.println("Cteni ze souboru ukonceno."); 
                    break;
                case "12":
                    System.out.print("Zadejte nazev SQL databaze pro ulozeni (napr. firma.db): ");
                    String dbZapis = sc.nextLine();
                    db.ZapisSQL(dbZapis);
                    System.out.println("Zapis do SQL ukoncen.");
                    break;
                case "13":
                	System.out.print("Touto akci se smazou aktualni zaznamy v DB. Chcete pokracovat? (ano/ne): ");
                    boolean stop2 = sc.nextLine().trim().equalsIgnoreCase("ano");
                    if(!stop2) {
                    	System.out.println("Navrat do menu");
                    	break;
                    }
                    System.out.print("Zadejte nazev SQL databaze k nacteni (napr. firma.db): ");
                    String dbNacist = sc.nextLine();
                    db.NacistSQL(dbNacist);
                    System.out.println("Cteni z SQL dokonceno.");
                    break;
                case "0":
                    bezi = false;
                    System.out.println("Ukoncuji program...");
                    break;
                default:
                    System.out.println("Neplatna volba, zkuste to znovu.");
            }
        }
        sc.close();
    }
    
    public int zkusNacistInt(Scanner sc, int min, int max) {
    
    	while (true) {
    		try {
    			String vstup = sc.nextLine();
    			int cislo=Integer.parseInt(vstup);
    			if(cislo<min || cislo>max) {
    				System.out.println("Chyba: Cislo musi byt mezi " + min + " - " + max);
    			}else {	
    				return cislo;
    			}	
    		} catch (NumberFormatException e) {
    			System.out.println("Nebylo zadano cele cislo");
    	    }	
			
		}
    	
    }
    
    public static void main(String[] args) {
		ObsluhaDB db = new ObsluhaDB();
		
		db.spustitMenu();
	}
    
}