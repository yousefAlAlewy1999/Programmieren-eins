package model;

//Yousef
import exception.BuchungsException;
import java.util.List;
import java.util.ArrayList;

public class Student extends Person {

    private static final String EMAIL_DOMAIN = "@hft-stuttgart.de";
    private static final String MATRIKEL_PRAEFIX = "200";
    private static final int    MATRIKEL_LAENGE  = 8;

    private int    studentId;
    private String matrikelnummer;

    public Student(int studentId, String name, String email,
                   String matrikelnummer) {
        super(name, email);
        validiereName(name);
        validiereEmail(email);
        validiereMatrikelnummer(matrikelnummer);

        this.studentId      = studentId;
        this.matrikelnummer = matrikelnummer;
    }

    private void validiereName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BuchungsException("Der Name darf nicht leer sein.");
        }
        if (name.matches(".*\\d.*")) {
            throw new BuchungsException(
                "Ungültiger Name '" + name + "'. Der Name darf keine Zahlen enthalten.");
        }
    }

    private void validiereEmail(String email) {
        if (email == null || !email.endsWith(EMAIL_DOMAIN)) {
            throw new BuchungsException(
                "Ungültige E-Mail '" + email + "'. Die E-Mail muss mit '"
                + EMAIL_DOMAIN + "' enden.");
        }
    }

    private void validiereMatrikelnummer(String matrikelnummer) {
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

    public int getStudentId() {
        return studentId;
    }

    public String getMatrikelnummer() {
        return matrikelnummer;
    }

    public List<Buchung> getBuchungen(List<Buchung> alleBuchungen) {
        List<Buchung> meineBuchungen = new ArrayList<>();
        for (Buchung b : alleBuchungen) {
            if (b.getStudent().getStudentId() == this.studentId) {
                meineBuchungen.add(b);
            }
        }
        return meineBuchungen;
    }

    @Override
    public String toString() {
        return "Student #" + studentId + " | " + getName()
               + " | Matrikelnr: " + matrikelnummer;
    }
}
