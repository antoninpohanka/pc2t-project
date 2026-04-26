package db;

import java.util.Scanner;

public class ObsluhaDB {

    private DB db;

    public ObsluhaDB() {
        this.db = new DB();

        // Pro testovani
        db.PridatZam("Jan", "Novak", 1980, false); // Bezp. spec. (ID 1)
        db.PridatZam("Petr", "Svoboda", 1992, true); // Dat. analytik (ID 2)
        db.PridatZam("Eva", "Nova", 1985, true); // Dat. analytik (ID 3)
        db.PridatSpol(1, 2, UrovSpol.DOBRA);
        db.PridatSpol(1, 3, UrovSpol.PRUMERNA);
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
            System.out.println("5) Zobrazit riziko spoluprace zamestnance (dle ID)");
            System.out.println("6) Abecedni vypis zamestnancu");
            System.out.println("7) Vypsat statistiky (zamestnanec s nejvc vazbami)");
            System.out.println("8) Vypis poctu zamestnancu ve skupinach");
            System.out.println("9) Zobrazit celou databazi");
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
                    int rok = Integer.parseInt(sc.nextLine());
                    System.out.print("Je datovy analytik? (ano/ne): ");
                    boolean jeDa = sc.nextLine().trim().equalsIgnoreCase("ano");
                    db.PridatZam(jmeno, prijmeni, rok, jeDa);
                    System.out.println("Zamestnanec uspesne pridan.");
                    break;
                case "2":
                    System.out.print("Zadejte vase ID: ");
                    int idZ = Integer.parseInt(sc.nextLine());
                    System.out.print("Zadejte ID kolegy: ");
                    int idK = Integer.parseInt(sc.nextLine());

                    // kontrola hned po zadani ID, aby se neresila uroven zbytecne
                    if (idZ == idK) {
                        System.out.println("Chyba: Zamestnanec nemuze spolupracovat sam se sebou.");
                        break; // ukonci tento case a vrati se do hlavniho menu
                    }

                    System.out.println("Vyberte uroven spoluprace (ciselne): ");
                    System.out.println("(1) Spatna ");
                    System.out.println("(2) Prumerna ");
                    System.out.println("(3) Dobra ");
                    int uroven = Integer.parseInt(sc.nextLine());

                    UrovSpol u = UrovSpol.PRUMERNA;
                    if (uroven == 1)
                        u = UrovSpol.SPATNA;
                    else if (uroven == 3)
                        u = UrovSpol.DOBRA;
                    else {
                        System.out.println("Chybne zadana uroven.");
                        break;
                    }

                    // Vypise uspesne zapsani jen tehdy, kdyz metoda vrati true (napr. pokud obe ID
                    // existuji); zmenil jsem metodu PridatSpol, aby vracela boolean
                    if (db.PridatSpol(idZ, idK, u)) {
                        System.out.println("Spoluprace zapsana.");
                    }
                    break;
                case "3":
                    System.out.print("Zadejte ID k odebrani: ");
                    int idO = Integer.parseInt(sc.nextLine());
                    db.OdebratZam(idO);
                    System.out.println("Pokus o smazani probehl.");
                    break;
                case "4":
                    System.out.print("Zadejte ID zamestnance k vyhledani: ");
                    int idV = Integer.parseInt(sc.nextLine());
                    db.NajitZam(idV);
                    break;
                case "5":
                    System.out.print("Zadejte ID zamestnance pro spusteni dovednosti: ");
                    int idD = Integer.parseInt(sc.nextLine());
                    db.DovedZam(idD);
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
}