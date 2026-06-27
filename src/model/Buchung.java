package model;

//Aldrin
public class Buchung {

    private int      buchungsId;
    private Student  student;
    private Raum     raum;
    private Zeitslot zeitslot;

    public Buchung(int buchungsId, Student student,
                   Raum raum, Zeitslot zeitslot) {
        this.buchungsId = buchungsId;
        this.student    = student;
        this.raum       = raum;
        this.zeitslot   = zeitslot;
    }

    public int getBuchungsId() {
        return buchungsId;
    }

    public Student getStudent() {
        return student;
    }

    public Raum getRaum() {
        return raum;
    }

    public Zeitslot getZeitslot() {
        return zeitslot;
    }

    public void stornieren() {
        raum.getBuchungen().remove(this);
        raum.kapazitaetErhoehen();
        System.out.println("Buchung #" + buchungsId
                           + " wurde erfolgreich storniert.");
    }

    @Override
    public String toString() {
        return "Buchung #" + buchungsId
               + " | " + student.getName()
               + " | Raum: " + raum.getName()
               + " | " + zeitslot.toString();
    }
}
