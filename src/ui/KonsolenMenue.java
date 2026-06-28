package ui;

// Importiert benötigte Klassen für Fehler, Manager, Modelle und Eingabe
import exception.BuchungsException;
import manager.BuchungsManager;
import model.Admin;
import model.Student;
import model.Zeitslot;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KonsolenMenue {

    // Hauptmanager, der Räume, Buchungen und Logik verwaltet
    private BuchungsManager manager;

    // Scanner für Konsoleneingaben
    private Scanner scanner;

    // Laufende ID für neu erstellte Studenten
    private int naechsteStudentId;

    // Liste aller Admins, die sich einloggen können
    private List<Admin> admins;

    // Konstruktor: Initialisiert Manager, Scanner, Student-ID und Admin-Liste
    public KonsolenMenue() {
        this.manager           = new BuchungsManager();
        this.scanner           = new Scanner(System.in);
        this.naechsteStudentId = 1;
        this.admins            = erstelleAdmins(); // Admins werden erzeugt
    }

    // Erstellt eine feste Liste von Admins
    private List<Admin> erstelleAdmins() {
        List<Admin> liste = new ArrayList<>();
        liste.add(new Admin(1, "Leon Mayer",  "leon.mayer@hft-stuttgart.de",  "Bibliothek", "leon123"));
        liste.add(new Admin(2, "Lisa Weber",  "lisa.weber@hft-stuttgart.de",  "Bibliothek", "lisa123"));
        liste.add(new Admin(3, "Sven Schmidt","sven.schmidt@hft-stuttgart.de","Bibliothek", "sven123"));
        return liste;
    }

    // Einstiegspunkt des Programms
    public static void main(String[] args) {
        KonsolenMenue menue = new KonsolenMenue();
        menue.start(); // Startet das Hauptmenü
    }

    // Hauptmenü: Auswahl zwischen Admin, Student oder Beenden
    public void start() {
        System.out.println("========================================");
        System.out.println("  Willkommen im Lernraum-Buchungssystem");
        System.out.println("========================================");

        boolean laufen = true;
        while (laufen) {
            System.out.println("\nWer bist du?");
            System.out.println("  1. Admin");
            System.out.println("  2. Student:in");
            System.out.println("  0. Beenden");
            System.out.print("Eingabe: ");

            int wahl = leseEingabe();
            switch (wahl) {
                case 1:
                    Admin admin = adminLogin(); // Admin-Login
                    if (admin != null) {
                        zeigeAdminMenue(admin); // Admin-Menü anzeigen
                    }
                    break;
                case 2:
                    Student student = studentLogin(); // Student-Login
                    zeigeStudentMenue(student);       // Student-Menü anzeigen
                    break;
                case 0:
                    laufen = false;
                    System.out.println("Auf Wiedersehen!");
                    break;
                default:
                    System.out.println("Ungültige Eingabe. Bitte 0, 1 oder 2 eingeben.");
            }
        }
        scanner.close(); // Scanner schließen
    }

    // Menü für Admins
    public void zeigeAdminMenue(Admin admin) {
        boolean laufen = true;
        while (laufen) {
            System.out.println("\n===== ADMIN-MENÜ =====");
            System.out.println("Eingeloggt als: " + admin.getName());
            System.out.println("  1. Raum anlegen");
            System.out.println("  2. Raum löschen");
            System.out.println("  3. Übersicht aller Räume und Buchungen");
            System.out.println("  4. Buchung stornieren");
            System.out.println("  0. Zurück");
            System.out.print("Eingabe: ");

            int wahl = leseEingabe();
            try {
                switch (wahl) {
                    case 1:
                        raumAnlegenDialog(); // Raum anlegen
                        break;
                    case 2:
                        raumLoeschenDialog(); // Raum löschen
                        break;
                    case 3:
                        manager.uebersichtsansicht(); // Übersicht anzeigen
                        break;
                    case 4:
                        buchungStornieren(); // Buchung stornieren
                        break;
                    case 0:
                        laufen = false;
                        break;
                    default:
                        System.out.println("Ungültige Eingabe.");
                }
            } catch (BuchungsException e) {
                System.out.println("Fehler: " + e.getMessage());
            }
        }
    }

    // Menü für Studenten
    public void zeigeStudentMenue(Student student) {
        boolean laufen = true;
        while (laufen) {
            System.out.println("\n===== STUDENTEN-MENÜ =====");
            System.out.println("Eingeloggt als: " + student.getName());
            System.out.println("  1. Alle Räume anzeigen");
            System.out.println("  2. Raum buchen");
            System.out.println("  3. Meine Buchungen anzeigen");
            System.out.println("  4. Eigene Buchung stornieren");
            System.out.println("  0. Zurück");
            System.out.print("Eingabe: ");

            int wahl = leseEingabe();
            try {
                switch (wahl) {
                    case 1:
                        verfuegbareRaeume(); // Räume anzeigen
                        break;
                    case 2:
                        buchungErstellenDialog(student); // Buchung erstellen
                        break;
                    case 3:
                        meineBuchungenAnzeigen(student); // Eigene Buchungen anzeigen
                        break;
                    case 4:
                        buchungStornieren(); // Buchung stornieren
                        break;
                    case 0:
                        laufen = false;
                        break;
                    default:
                        System.out.println("Ungültige Eingabe.");
                }
            } catch (BuchungsException e) {
                System.out.println("Fehler: " + e.getMessage());
            }
        }
    }

    // Admin-Login mit maximal 3 Versuchen
    private Admin adminLogin() {
        System.out.println("\n--- Admin Login ---");
        int versuche = 0;

        while (versuche < 3) {
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Fehler: Der Name darf nicht leer sein.");
                versuche++;
                continue;
            }

            System.out.print("Passwort: ");
            String passwort = scanner.nextLine().trim();

            if (passwort.isEmpty()) {
                System.out.println("Fehler: Das Passwort darf nicht leer sein.");
                versuche++;
                continue;
            }

            Admin gefundenerAdmin = findeAdmin(name, passwort);
            if (gefundenerAdmin == null) {
                System.out.println("Fehler: Name oder Passwort ist falsch.");
                versuche++;
                continue;
            }

            System.out.println("Willkommen, " + gefundenerAdmin.getName() + "!");
            return gefundenerAdmin;
        }

        System.out.println("Zu viele Fehlversuche. Zurück zum Hauptmenü.");
        return null;
    }

    // Sucht Admin anhand Name + Passwort
    private Admin findeAdmin(String name, String passwort) {
        for (Admin a : admins) {
            if (a.getName().equals(name) && a.getPasswort().equals(passwort)) {
                return a;
            }
        }
        return null;
    }

    // Student-Login: Validierung erfolgt durch Student-Konstruktor
    private Student studentLogin() {
        System.out.println("\n--- Student:in Login ---");

        while (true) {
            try {
                System.out.print("Name: ");
                String name = scanner.nextLine().trim();

                System.out.print("E-Mail (z.B. max.mustermann@hft-stuttgart.de): ");
                String email = scanner.nextLine().trim();

                System.out.print("Matrikelnummer (8 Ziffern, beginnt mit 200): ");
                String matrikelnummer = scanner.nextLine().trim();

                Student student = new Student(
                    naechsteStudentId, name, email, matrikelnummer);
                naechsteStudentId++;

                System.out.println("Willkommen, " + student.getName() + "!");
                return student;
            } catch (BuchungsException e) {
                System.out.println("Fehler: " + e.getMessage());
                System.out.println("Bitte erneut eingeben.");
            }
        }
    }

    // Dialog zum Anlegen eines Raumes
    private void raumAnlegenDialog() {
        System.out.print("Raumname (Format Gebäude/Raumnummer, z.B. 3/014): ");
        String name = scanner.nextLine().trim();

        System.out.print("Kapazität (Anzahl Plätze): ");
        int kapazitaet = leseEingabe();

        System.out.print("Ausstattung (z.B. Beamer, PC): ");
        String ausstattung = scanner.nextLine().trim();

        System.out.println("Öffnungszeitfenster festlegen:");
        System.out.print("  Datum (z.B. 12.06.2026): ");
        String datum = scanner.nextLine().trim();
        System.out.print("  Startzeit (z.B. 9:45): ");
        String start = scanner.nextLine().trim();
        System.out.print("  Endzeit (z.B. 14:30): ");
        String ende = scanner.nextLine().trim();
        Zeitslot oeffnungszeitfenster = new Zeitslot(datum, start, ende);

        manager.raumAnlegen(name, kapazitaet, ausstattung, oeffnungszeitfenster);
    }

    // Dialog zum Löschen eines Raumes
    private void raumLoeschenDialog() {
        System.out.print("Raum-ID zum Löschen: ");
        int id = leseEingabe();
        manager.raumLoeschen(id);
    }

    // Dialog zum Stornieren einer Buchung
    private void buchungStornieren() {
        System.out.print("Buchungs-ID zum Stornieren: ");
        int id = leseEingabe();
        manager.buchungStornieren(id);
    }

    // Zeigt alle verfügbaren Räume
    private void verfuegbareRaeume() {
        manager.verfuegbareRaeumeAnzeigen();
    }

    // Dialog zum Erstellen einer Buchung
    private void buchungErstellenDialog(Student student) {
        Zeitslot slot = zeitslotEingabe();

        System.out.print("Raum-ID: ");
        int raumId = leseEingabe();

        manager.buchungErstellen(student, raumId, slot);
    }

    // Zeigt alle Buchungen des eingeloggten Studenten
    private void meineBuchungenAnzeigen(Student student) {
        System.out.println("\n===== MEINE BUCHUNGEN =====");
        var meineBuchungen = student.getBuchungen(manager.getBuchungen());
        if (meineBuchungen.isEmpty()) {
            System.out.println("Keine Buchungen vorhanden.");
        } else {
            for (var b : meineBuchungen) {
                System.out.println("  " + b);
            }
        }
        System.out.println("===========================");
    }

    // Eingabe eines Zeitslots
    private Zeitslot zeitslotEingabe() {
        System.out.print("Datum (z.B. 12.06.2026): ");
        String datum = scanner.nextLine().trim();

        System.out.print("Startzeit (z.B. 9:45): ");
        String start = scanner.nextLine().trim();

        System.out.print("Endzeit (z.B. 14:30): ");
        String ende = scanner.nextLine().trim();

        return new Zeitslot(datum, start, ende);
    }

    // Liest eine Zahl ein und behandelt falsche Eingaben
    public int leseEingabe() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Bitte eine gültige Zahl eingeben!");
            return -1;
        }
    }
}
