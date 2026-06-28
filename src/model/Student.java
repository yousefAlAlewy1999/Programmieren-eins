package model;

// Yousef – Klasse Student repräsentiert einen Studenten im System
import exception.BuchungsException;
import java.util.List;
import java.util.ArrayList;

public class Student extends Person {   // Student erbt von Person (Name + Email)

    // Vorgeschriebene Hochschul-Domain für E-Mails
    private static final String EMAIL_DOMAIN = "@hft-stuttgart.de";

    // Jede Matrikelnummer muss mit "200" beginnen
    private static final String MATRIKEL_PRAEFIX = "200";

    // Matrikelnummer muss genau 8 Ziffern haben
    private static final int MATRIKEL_LAENGE = 8;

    // Interne eindeutige ID des Studenten (z. B. Datenbank-ID)
    private int studentId;

    // Offizielle Matrikelnummer des Studenten
    private String matrikelnummer;

    // Konstruktor: erstellt einen Student und validiert alle Eingaben
    public Student(int studentId, String name, String email,
                   String matrikelnummer) {

        super(name, email); // Übergibt Name und Email an die Oberklasse Person

        // Validierungen für korrekte Eingaben
        validiereName(name);
        validiereEmail(email);
        validiereMatrikelnummer(matrikelnummer);

        // Wenn alles gültig ist, werden die Werte gesetzt
        this.studentId      = studentId;
        this.matrikelnummer = matrikelnummer;
    }

    // Prüft, ob der Name gültig ist
    private void validiereName(String name) {

        // Name darf nicht leer sein oder nur aus Leerzeichen bestehen
        if (name == null || name.trim().isEmpty()) {
            throw new BuchungsException("Der Name darf nicht leer sein.");
        }

        // Name darf keine Zahlen enthalten (RegEx: .*\\d.* bedeutet: enthält eine Ziffer)
        if (name.matches(".*\\d.*")) {
            throw new BuchungsException(
                "Ungültiger Name '" + name + "'. Der Name darf keine Zahlen enthalten.");
        }
    }

    // Prüft, ob die E-Mail gültig ist
    private void validiereEmail(String email) {

        // E-Mail darf nicht null sein und muss mit der Hochschul-Domain enden
        if (email == null || !email.endsWith(EMAIL_DOMAIN)) {
            throw new BuchungsException(
                "Ungültige E-Mail '" + email + "'. Die E-Mail muss mit '"
                + EMAIL_DOMAIN + "' enden.");
        }
    }

    // Prüft, ob die Matrikelnummer gültig ist
    private void validiereMatrikelnummer(String matrikelnummer) {

        // Bedingungen:
        // - nicht null
        // - genau 8 Zeichen
        // - nur Ziffern (RegEx: \\d{8})
        // - beginnt mit "200"
        if (matrikelnummer == null
                || matrikelnummer.length() != MATRIKEL_LAENGE
                || !matrikelnummer.matches("\\d{" + MATRIKEL_LAENGE + "}")
                || !matrikelnummer.startsWith(MATRIKEL_PRAEFIX)) {

            throw new BuchungsException(
                "Ungültige Matrikelnummer '" + matrikelnummer
                + "'. Sie muss genau " + MATRIKEL_LAENGE
                + " Ziffern haben und mit '" + MATRIKEL_PRAEFIX + "' beginnen.");
        }
    }

    // Getter für die interne Student-ID
    public int getStudentId() {
        return studentId;
    }

    // Getter für die Matrikelnummer
    public String getMatrikelnummer() {
        return matrikelnummer;
    }

    // Gibt alle Buchungen zurück, die zu diesem Studenten gehören
    public List<Buchung> getBuchungen(List<Buchung> alleBuchungen) {

        // Neue Liste für die Buchungen dieses Studenten
        List<Buchung> meineBuchungen = new ArrayList<>();

        // Schleife über alle Buchungen im System
        for (Buchung b : alleBuchungen) {

            // Prüft: Gehört die Buchung zu diesem Studenten?
            // Vergleich über studentId (eindeutig)
            if (b.getStudent().getStudentId() == this.studentId) {

                // Wenn ja → Buchung hinzufügen
                meineBuchungen.add(b);
            }
        }

        // Rückgabe aller Buchungen dieses Studenten
        return meineBuchungen;
    }

    // Schöne Ausgabe des Studenten für Konsole, Debugging oder Logging
    @Override
    public String toString() {
        return "Student #" + studentId + " | " + getName()
               + " | Matrikelnr: " + matrikelnummer;
    }
}
