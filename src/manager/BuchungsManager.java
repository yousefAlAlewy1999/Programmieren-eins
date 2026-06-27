package manager;

import exception.BuchungsException;
import model.Buchung;
import model.Raum;
import model.Student;
import model.Zeitslot;

import java.util.ArrayList;
import java.util.List;

public class BuchungsManager {
//Yousef
    private List<Raum>    raeume;
    private List<Buchung> buchungen;
    private int           naechsteBuchungsId;

    public BuchungsManager() {
        this.raeume             = new ArrayList<>();
        this.buchungen          = new ArrayList<>();
        this.naechsteBuchungsId = 1;
    }

    public List<Raum> getRaeume() {
        return raeume;
    }

    public List<Buchung> getBuchungen() {
        return buchungen;
    }

    public void raumAnlegen(String name, int kapazitaet, String ausstattung,
                            Zeitslot oeffnungszeitfenster) {
        for (Raum r : raeume) {
            if (r.getName().equals(name)
                    && r.getOeffnungszeitfenster().getDatum().equals(oeffnungszeitfenster.getDatum())) {
                throw new BuchungsException(
                    "Raum '" + name + "' existiert am " + oeffnungszeitfenster.getDatum()
                    + " bereits.");
            }
        }

        int neueId = findeNaechsteFreieRaumId();
        Raum neuerRaum = new Raum(neueId, name, kapazitaet,
                                   ausstattung, oeffnungszeitfenster);
        raeume.add(neuerRaum);
        System.out.println("Raum '" + name + "' wurde erfolgreich angelegt.");
    }

    public void raumLoeschen(int raumId) {
        Raum zuLoeschen = findeRaum(raumId);
        raeume.remove(zuLoeschen);
        
        buchungen.removeIf(b -> b.getRaum().getRaumId() == raumId);
        System.out.println("Raum #" + raumId + " wurde erfolgreich gelöscht.");
    }

    public Buchung buchungErstellen(Student student, int raumId, Zeitslot zeitslot) {
        Raum raum = findeRaum(raumId);

        if (!raum.liegtImOeffnungszeitfenster(zeitslot)) {
            throw new BuchungsException(
                "Der gewünschte Zeitslot liegt außerhalb der Öffnungszeit von Raum '"
                + raum.getName() + "' (" + raum.getOeffnungszeitfenster() + ").");
        }

        if (!raum.isVerfuegbar()) {
            throw new BuchungsException(
                "Raum '" + raum.getName() + "' hat keinen Platz mehr.");
        }

        for (Buchung b : buchungen) {
            if (b.getStudent().getStudentId() == student.getStudentId()
                    && b.getRaum().getRaumId() == raumId
                    && b.getZeitslot().ueberschneidetSich(zeitslot)) {
                throw new BuchungsException(
                    "Sie haben diesen Zeitslot bereits gebucht.");
            }
        }

        raum.kapazitaetVerringern();

        Buchung neueBuchung = new Buchung(
            naechsteBuchungsId, student, raum, zeitslot);
        raum.getBuchungen().add(neueBuchung);
        buchungen.add(neueBuchung);
        naechsteBuchungsId++;
        System.out.println("Buchung erfolgreich: " + neueBuchung);
        return neueBuchung;
    }
	//Yousef
//Aldrin
    public void buchungStornieren(int buchungsId) {
        Buchung zuStornieren = findeBuchung(buchungsId);
        zuStornieren.stornieren();
        buchungen.remove(zuStornieren);
    }

    public void uebersichtsansicht() {
        if (raeume.isEmpty()) {
            System.out.println("Keine Räume vorhanden.");
            return;
        }
        System.out.println("\n===== ÜBERSICHT ALLE RÄUME =====");
        for (Raum r : raeume) {
            System.out.println("\n" + r);
            if (r.getBuchungen().isEmpty()) {
                System.out.println("  → Keine Buchungen");
            } else {
                for (Buchung b : r.getBuchungen()) {
                    System.out.println("  → " + b);
                }
            }
        }
        System.out.println("\n================================");
    }

    public void verfuegbareRaeumeAnzeigen() {
        System.out.println("\n===== ALLE RÄUME =====");
        if (raeume.isEmpty()) {
            System.out.println("Keine Räume vorhanden.");
        } else {
            for (Raum r : raeume) {
                System.out.println(r);
            }
        }
        System.out.println("=======================");
    }

    private int findeNaechsteFreieRaumId() {
        int id = 1;
        while (raumIdVergeben(id)) {
            id++;
        }
        return id;
    }

    private boolean raumIdVergeben(int id) {
        for (Raum r : raeume) {
            if (r.getRaumId() == id) {
                return true;
            }
        }
        return false;
    }

    private Raum findeRaum(int raumId) {
        for (Raum r : raeume) {
            if (r.getRaumId() == raumId) {
                return r;
            }
        }
        throw new BuchungsException("Raum #" + raumId + " nicht gefunden.");
    }

    private Buchung findeBuchung(int buchungsId) {
        for (Buchung b : buchungen) {
            if (b.getBuchungsId() == buchungsId) {
                return b;
            }
        }
        throw new BuchungsException(
            "Buchung #" + buchungsId + " nicht gefunden.");
    }
}
//Aldrin
