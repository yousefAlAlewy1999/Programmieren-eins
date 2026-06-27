package model;

//Aldrin
import exception.BuchungsException;
import java.util.ArrayList;
import java.util.List;

public class Raum {

    private static final int GEBAEUDE_MIN     = 1;
    private static final int GEBAEUDE_MAX     = 8;
    private static final int RAUMNUMMER_MIN   = 1;
    private static final int RAUMNUMMER_MAX   = 314;

    private int           raumId;
    private String        name;
    private int           kapazitaet;
    private String        ausstattung;
    private List<Buchung> buchungen;
    private Zeitslot      oeffnungszeitfenster;

    public Raum(int raumId, String name, int kapazitaet, String ausstattung,
                Zeitslot oeffnungszeitfenster) {
        if (kapazitaet <= 0) {
            throw new BuchungsException(
                "Kapazität muss größer als 0 sein.");
        }
        validiereName(name);
        if (oeffnungszeitfenster == null) {
            throw new BuchungsException("Öffnungszeitfenster darf nicht leer sein.");
        }

        this.raumId               = raumId;
        this.name                 = name;
        this.kapazitaet           = kapazitaet;
        this.ausstattung          = ausstattung;
        this.buchungen            = new ArrayList<>();
        this.oeffnungszeitfenster = oeffnungszeitfenster;
    }

    private void validiereName(String name) {
        if (name == null || !name.matches("\\d{1,2}/\\d{2,3}")) {
            throw new BuchungsException(
                "Ungültiger Raumname '" + name
                + "'. Format muss 'Gebäude/Raumnummer' sein, z.B. '3/014'.");
        }

        String[] teile = name.split("/");
        int gebaeude;
        int raumnummer;

        try {
            gebaeude   = Integer.parseInt(teile[0]);
            raumnummer = Integer.parseInt(teile[1]);
        } catch (NumberFormatException e) {
            throw new BuchungsException(
                "Ungültiger Raumname '" + name
                + "'. Gebäude und Raumnummer müssen Zahlen sein.");
        }

        if (gebaeude < GEBAEUDE_MIN || gebaeude > GEBAEUDE_MAX) {
            throw new BuchungsException(
                "Ungültiges Gebäude '" + gebaeude + "'. Erlaubt sind nur Gebäude "
                + GEBAEUDE_MIN + " bis " + GEBAEUDE_MAX + ".");
        }

        if (raumnummer < RAUMNUMMER_MIN || raumnummer > RAUMNUMMER_MAX) {
            throw new BuchungsException(
                "Ungültige Raumnummer '" + teile[1] + "'. Erlaubt sind nur Raumnummern von "
                + String.format("%02d", RAUMNUMMER_MIN) + " bis " + RAUMNUMMER_MAX + ".");
        }
    }

    public int getRaumId() {
        return raumId;
    }

    public String getName() {
        return name;
    }

    public int getKapazitaet() {
        return kapazitaet;
    }

    public String getAusstattung() {
        return ausstattung;
    }

    public List<Buchung> getBuchungen() {
        return buchungen;
    }

    public Zeitslot getOeffnungszeitfenster() {
        return oeffnungszeitfenster;
    }

    public boolean liegtImOeffnungszeitfenster(Zeitslot slot) {
        if (!slot.getDatum().equals(oeffnungszeitfenster.getDatum())) {
            return false;
        }
        return slot.getStartzeitInMinuten() >= oeffnungszeitfenster.getStartzeitInMinuten()
            && slot.getEndzeitInMinuten()   <= oeffnungszeitfenster.getEndzeitInMinuten();
    }

    public boolean isVerfuegbar() {
        return kapazitaet > 0;
    }

    public void kapazitaetVerringern() {
        if (kapazitaet <= 0) {
            throw new BuchungsException(
                "Raum '" + name + "' hat keinen Platz mehr.");
        }
        kapazitaet--;
    }

    public void kapazitaetErhoehen() {
        kapazitaet++;
    }

    @Override
    public String toString() {
        return "Raum #" + raumId
               + " | " + name
               + " | Kapazität: " + kapazitaet
               + " | Ausstattung: " + ausstattung
               + " | Öffnungszeit: " + oeffnungszeitfenster;
    }
}
