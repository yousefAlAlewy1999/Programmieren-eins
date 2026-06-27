package model;

//Aldrin
import exception.BuchungsException;

public class Zeitslot {

    private static final int GEBAEUDE_OEFFNET_MINUTEN   = 8 * 60;  
    private static final int GEBAEUDE_SCHLIESST_MINUTEN = 22 * 60; 

    private String datum;
    private String startzeit;   
    private String endzeit;     

    public Zeitslot(String datum, String startzeit, String endzeit) {
        int startMin = parseZeitZuMinuten(startzeit);
        int endMin   = parseZeitZuMinuten(endzeit);

        if (endMin <= startMin) {
            throw new BuchungsException(
                "Ungültiger Zeitslot: Endzeit muss nach Startzeit liegen.");
        }
        if (startMin < GEBAEUDE_OEFFNET_MINUTEN || endMin > GEBAEUDE_SCHLIESST_MINUTEN) {
            throw new BuchungsException(
                "Ungültiger Zeitslot: Die Gebäude sind nur von 8:00 bis 22:00 Uhr geöffnet.");
        }

        this.datum     = datum;
        this.startzeit = startzeit;
        this.endzeit   = endzeit;
    }

    private int parseZeitZuMinuten(String zeit) {
        if (zeit == null || !zeit.matches("\\d{1,2}:\\d{2}")) {
            throw new BuchungsException(
                "Ungültiges Zeitformat '" + zeit + "'. Erwartet wird z.B. '9:45' oder '14:30'.");
        }
        String[] teile = zeit.split(":");
        int stunde = Integer.parseInt(teile[0]);
        int minute = Integer.parseInt(teile[1]);

        if (stunde < 0 || stunde > 23 || minute < 0 || minute > 59) {
            throw new BuchungsException(
                "Ungültige Uhrzeit '" + zeit + "'. Stunde muss 0-23, Minute 0-59 sein.");
        }
        return stunde * 60 + minute;
    }

    public String getDatum() {
        return datum;
    }

    public String getStartzeit() {
        return startzeit;
    }

    public String getEndzeit() {
        return endzeit;
    }

    public int getStartzeitInMinuten() {
        return parseZeitZuMinuten(startzeit);
    }

    public int getEndzeitInMinuten() {
        return parseZeitZuMinuten(endzeit);
    }

    public boolean ueberschneidetSich(Zeitslot anderer) {
        if (!this.datum.equals(anderer.datum)) {
            return false;
        }
        return this.getStartzeitInMinuten() < anderer.getEndzeitInMinuten()
            && anderer.getStartzeitInMinuten() < this.getEndzeitInMinuten();
    }

    @Override
    public String toString() {
        return datum + " | " + startzeit + " - " + endzeit + " Uhr";
    }
}
