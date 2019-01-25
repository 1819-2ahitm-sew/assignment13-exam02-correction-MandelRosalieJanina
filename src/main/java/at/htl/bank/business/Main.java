package at.htl.bank.business;

import at.htl.bank.model.BankKonto;
import at.htl.bank.model.GiroKonto;
import at.htl.bank.model.SparKonto;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Legen Sie eine statische Liste "konten" an, in der Sie die einzelnen Konten speichern
 */
public class Main {

    // die Konstanten sind package-scoped wegen der Unit-Tests
    static final double GEBUEHR = 0.02;
    static final double ZINSSATZ = 3.0;

    static final String KONTENDATEI = "erstellung.csv";
    static final String BUCHUNGSDATEI = "buchungen.csv";
    static final String ERGEBNISDATEI = "ergebnis.csv";

    static ArrayList<BankKonto> kontenGesammt = new ArrayList<>();

    /**
     * Führen Sie die drei Methoden erstelleKonten, fuehreBuchungenDurch und
     * findKontoPerName aus
     *
     * @param args
     */
    public static void main(String[] args) {


        erstelleKonten(KONTENDATEI);
        fuehreBuchungenDurch(BUCHUNGSDATEI);
        schreibeKontostandInDatei(ERGEBNISDATEI);

    }

    /**
     * Lesen Sie aus der Datei (erstellung.csv) die Konten ein.
     * Je nach Kontentyp erstellen Sie ein Spar- oder Girokonto.
     * Gebühr und Zinsen sind als Konstanten angegeben.
     * <p>
     * Nach dem Anlegen der Konten wird auf der Konsole folgendes ausgegeben:
     * Erstellung der Konten beendet
     *
     * @param datei KONTENDATEI
     */
    private static void erstelleKonten(String datei) {
        String[] parts;
        double anfangsBetrag;
        String name;
        String kontoTyp;

        try (Scanner scanner = new Scanner(new FileReader(datei))) {
            scanner.nextLine();


            while (scanner.hasNextLine()) {

                parts = scanner.nextLine().split(";");

                kontoTyp = parts[0];
                name = parts[1];
                anfangsBetrag = Double.parseDouble(parts[2]);

                if (kontoTyp.equals("Girokonto")) {
                    GiroKonto giroKonto = new GiroKonto(name, anfangsBetrag, GEBUEHR);
                    kontenGesammt.add(giroKonto);

                } else if (kontoTyp.equals("Sparkonto")) {
                    SparKonto sparKonto = new SparKonto(name, anfangsBetrag, ZINSSATZ);
                    kontenGesammt.add(sparKonto);

                }


            }


        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }


        for (int i = 0; i < kontenGesammt.size(); i++) {
            System.out.println(kontenGesammt.get(i));
        }

        System.out.println("Erstellung der Konten beendet");
    }

    /**
     * Die einzelnen Buchungen werden aus der Datei eingelesen.
     * Es wird aus der Liste "konten" jeweils das Bankkonto für
     * kontoVon und kontoNach gesucht.
     * Anschließend wird der Betrag vom kontoVon abgebucht und
     * der Betrag auf das kontoNach eingezahlt
     * <p>
     * Nach dem Durchführen der Buchungen wird auf der Konsole folgendes ausgegeben:
     * Buchung der Beträge beendet
     * <p>
     * Tipp: Verwenden Sie hier die Methode 'findeKontoPerName(String name)'
     *
     * @param datei BUCHUNGSDATEI
     */
    private static void fuehreBuchungenDurch(String datei) {

        String[] parts;
        double betrag;


        try (Scanner scanner = new Scanner(new FileReader(datei))) {
            scanner.nextLine();


            while (scanner.hasNextLine()) {

                parts = scanner.nextLine().split(";");

                BankKonto kontoVon = findeKontoPerName(parts[0]);
                BankKonto kontoNach = findeKontoPerName(parts[1]);


                betrag = Double.parseDouble(parts[2]);


                kontoVon.abheben(betrag);
                kontoNach.einzahlen(betrag);

            }


        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }



        System.out.println("Buchung der Beträge beendet");
    }

    /**
     * Es werden die Kontostände sämtlicher Konten in die ERGEBNISDATEI
     * geschrieben. Davor werden bei Sparkonten noch die Zinsen dem Konto
     * gutgeschrieben
     * <p>
     * Die Datei sieht so aus:
     * <p>
     * name;kontotyp;kontostand
     * Susi;SparKonto;875.5
     * Mimi;GiroKonto;949.96
     * Hans;GiroKonto;1199.96
     * <p>
     * Vergessen Sie nicht die Überschriftenzeile
     * <p>
     * Nach dem Schreiben der Datei wird auf der Konsole folgendes ausgegeben:
     * Ausgabe in Ergebnisdatei beendet
     *
     * @param datei ERGEBNISDATEI
     */
    private static void schreibeKontostandInDatei(String datei) {



        try(PrintWriter printWriter = new PrintWriter(new FileWriter(datei))){

            for (int i = 0; i < kontenGesammt.size(); i++) {


                if (kontenGesammt.get(i) instanceof  SparKonto){
                    ((SparKonto) kontenGesammt.get(i)).zinsenAnrechen();
                    printWriter.printf("%s;Sparkonto;%.2f%n", kontenGesammt.get(i).getName(), kontenGesammt.get(i).getKontoStand());
                }else{
                    printWriter.printf("%s;Girokonto;%.2f%n", kontenGesammt.get(i).getName(), kontenGesammt.get(i).getKontoStand());

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Ausgabe in Ergebnsidatei beendet");
    }

    /**
     */
    /**
     * Durchsuchen Sie die Liste "konten" nach dem ersten Konto mit dem als Parameter
     * übergebenen Namen
     *
     * @param name
     * @return Bankkonto mit dem gewünschten Namen oder NULL, falls der Namen
     * nicht gefunden wird
     */
    public static BankKonto findeKontoPerName(String name) {

        BankKonto konto = null;


        for (int i = 0; i < kontenGesammt.size() ; i++) {
            if (kontenGesammt.get(i).getName().equals(name)){
                konto = kontenGesammt.get(i);
            }
        }
        return konto;
    }

}
