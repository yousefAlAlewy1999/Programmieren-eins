package ui;

import exception.BuchungsException;
import manager.BuchungsManager;
import model.Admin;
import model.Student;
import model.Zeitslot;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KonsolenMenue {
	
	//Yousef

    private BuchungsManager manager;
    private Scanner         scanner;
    private int             naechsteStudentId;
    private List<Admin>     admins;

    public KonsolenMenue() {
        this.manager           = new BuchungsManager();
        this.scanner           = new Scanner(System.in);
        this.naechsteStudentId = 1;
        this.admins            = erstelleAdmins();
    }

    private List<Admin> erstelleAdmins() {
        List<Admin> liste = new ArrayList<>();
        liste.add(new Admin(1, "Leon Mayer",  "leon.mayer@hft-stuttgart.de",  "Bibliothek", "leon123"));
        liste.add(new Admin(2, "Lisa Weber",  "lisa.weber@hft-stuttgart.de",  "Bibliothek", "lisa123"));
        liste.add(new Admin(3, "Sven Schmidt","sven.schmidt@hft-stuttgart.de","Bibliothek", "sven123"));
        return liste;
    }

    public static void main(String[] args) {
        KonsolenMenue menue = new KonsolenMenue();
        menue.start();
    }

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
                    Admin admin = adminLogin();
                    if (admin != null) {
                        zeigeAdminMenue(admin);
                    }
                    break;
                case 2:
                    Student student = studentLogin();
                    zeigeStudentMenue(student);
                    break;
                case 0:
                    laufen = false;
                    System.out.println("Auf Wiedersehen!");
                    break;
                default:
                    System.out.println("Ungültige Eingabe. Bitte 0, 1 oder 2 eingeben.");
            }
        }
        scanner.close();
    }

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
                        raumAnlegenDialog();
                        break;
                    case 2:
                        raumLoeschenDialog();
                        break;
                    case 3:
                        manager.uebersichtsansicht();
                        break;
                    case 4:
                        buchungStornieren();
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
                        verfuegbareRaeume();
                        break;
                    case 2:
                        buchungErstellenDialog(student);
                        break;
                    case 3:
                        meineBuchungenAnzeigen(student);
                        break;
                    case 4:
                        buchungStornieren();
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
	//Yousef
//Aldrin
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

    private Admin findeAdmin(String name, String passwort) {
        for (Admin a : admins) {
            if (a.getName().equals(name) && a.getPasswort().equals(passwort)) {
                return a;
            }
        }
        return null;
    }

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

    private void raumLoeschenDialog() {
        System.out.print("Raum-ID zum Löschen: ");
        int id = leseEingabe();
        manager.raumLoeschen(id);
    }

    private void buchungStornieren() {
        System.out.print("Buchungs-ID zum Stornieren: ");
        int id = leseEingabe();
        manager.buchungStornieren(id);
    }

    private void verfuegbareRaeume() {
        manager.verfuegbareRaeumeAnzeigen();
    }

    private void buchungErstellenDialog(Student student) {
        Zeitslot slot = zeitslotEingabe();

        System.out.print("Raum-ID: ");
        int raumId = leseEingabe();

        manager.buchungErstellen(student, raumId, slot);
    }

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

    private Zeitslot zeitslotEingabe() {
        System.out.print("Datum (z.B. 12.06.2026): ");
        String datum = scanner.nextLine().trim();

        System.out.print("Startzeit (z.B. 9:45): ");
        String start = scanner.nextLine().trim();

        System.out.print("Endzeit (z.B. 14:30): ");
        String ende = scanner.nextLine().trim();

        return new Zeitslot(datum, start, ende);
    }

    public int leseEingabe() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Bitte eine gültige Zahl eingeben!");
            return -1;
        }
    }
}
//Aldrin
