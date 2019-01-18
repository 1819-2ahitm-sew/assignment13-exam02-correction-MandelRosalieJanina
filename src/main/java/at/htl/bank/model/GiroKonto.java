package at.htl.bank.model;

public class GiroKonto extends BankKonto {

    private double gebuehr;

    public GiroKonto(String name, double gebuehr) {
        super(name);
        this.gebuehr = gebuehr;
    }

    public GiroKonto(String name, double anfangsBestand, double gebuehr) {
        super(name, anfangsBestand);
        this.gebuehr = gebuehr;

    }


    @Override
    public void einzahlen(double betrag) {

        //kontoStand = kontoStand + (betrag - gebuehr);
        kontoStand += betrag;
        kontoStand -= gebuehr;
    }

    @Override
    public void abheben(double betrag) {

        //kontoStand = kontoStand - (betrag -gebuehr);
        kontoStand -= betrag;
        kontoStand -= gebuehr;
    }

    @Override
    public String toString() {
        return "GiroKonto";
    }
}
