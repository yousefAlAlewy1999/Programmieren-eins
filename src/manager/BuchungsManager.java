package manager;

import exception.BuchungsException;
import model.Buchung;
import model.Raum;
import model.Student;
import model.Zeitslot;

import java.util.ArrayList;
import java.util.List;

public class BuchungsManager {
// Yousef

    // Liste aller Räume im System
    private List<Raum> raeume;

    // Liste aller Buchungen im System
    private List<Buchung> buchungen;

    // Laufende ID für neue Buchungen
    private int naechsteBuchungsId;

    // Konstruktor: Initialisiert die Listen und setzt die erste Buchungs-ID
    public BuchungsManager() {
        this.raeume             = new ArrayList<>();
        this.buchungen          = new ArrayList<>();
        this.naechsteBuchungsId = 1;
    }

    // Getter für alle Räume
    public List<Raum> getRaeume() {
        return raeume;
    }

    // Getter für alle Buchungen
    public List<Buchung> getBuchungen() {
        return buchungen;
    }

    // Legt einen neuen Raum an
    public void raumAnlegen(String name, int kapazitaet, String ausstattung,
                            Zeitslot oeffnungszeitfenster) {

        // Prüft, ob der Raum am gleichen Datum bereits existiert
        for (Raum r : raeume) {
            if (r.getName().equals(name)
                    && r.getOeffnungszeitfenster().getDatum().equals(oeffnungszeitfenster.getDatum())) {

                // Fehler, wenn Raum doppelt existiert
                throw new BuchungsException(
                    "Raum '" + name + "' existiert am " + oeffnungszeitfenster.getDatum()
                    + " bereits.");
            }
        }

        // Neue Raum-ID finden
        int neueId = findeNaechsteFreieRaumId();

        // Raum erzeugen
        Raum neuerRaum = new Raum(neueId, name, kapazitaet,
                                   ausstattung, oeffnungszeitfenster);

        // Raum zur Liste hinzufügen
        raeume.add(neuerRaum);

        System.out.println("Raum '" + name + "' wurde erfolgreich angelegt.");
    }

    // Löscht einen Raum anhand seiner ID
    public void raumLoeschen(int raumId) {

        // Raum suchen
        Raum zuLoeschen = findeRaum(raumId);

        // Raum entfernen
        raeume.remove(zuLoeschen);

        // Alle Buchungen dieses Raumes entfernen
        buchungen.removeIf(b -> b.getRaum().getRaumId() == raumId);

        System.out.println("Raum #" + raumId + " wurde erfolgreich gelöscht.");
    }

    // Erstellt eine neue Buchung
    public Buchung buchungErstellen(Student student, int raumId, Zeitslot zeitslot) {

        // Raum anhand ID finden
        Raum raum = findeRaum(raumId);

        // Prüfen: Liegt der Zeitslot innerhalb der Öffnungszeit?
        if (!raum.liegtImOeffnungszeitfenster(zeitslot)) {
            throw new BuchungsException(
                "Der gewünschte Zeitslot liegt außerhalb der Öffnungszeit von Raum '"
                + raum.getName() + "' (" + raum.getOeffnungszeitfenster() + ").");
        }

        // Prüfen: Hat der Raum noch freie Plätze?
        if (!raum.isVerfuegbar()) {
            throw new BuchungsException(
                "Raum '" + raum.getName() + "' hat keinen Platz mehr.");
        }

        // Prüfen: Hat der Student diesen Zeitslot bereits gebucht?
        for (Buchung b : buchungen) {
            if (b.getStudent().getStudentId() == student.getStudentId()
                    && b.getRaum().getRaumId() == raumId
                    && b.getZeitslot().ueberschneidetSich(zeitslot)) {

                throw new BuchungsException(
                    "Sie haben diesen Zeitslot bereits gebucht.");
            }
        }

        // Kapazität des Raumes verringern
        raum.kapazitaetVerringern();

        // Neue Buchung erzeugen
        Buchung neueBuchung = new Buchung(
            naechsteBuchungsId, student, raum, zeitslot);

        // Buchung dem Raum hinzufügen
        raum.getBuchungen().add(neueBuchung);

        // Buchung der globalen Liste hinzufügen
        buchungen.add(neueBuchung);

        // Buchungs-ID erhöhen
        naechsteBuchungsId++;

        System.out.println("Buchung erfolgreich: " + neueBuchung);

        return neueBuchung;
    }
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
