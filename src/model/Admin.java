package model;

//Yousef
public class Admin extends Person {

    private int    adminId;
    private String bereich;
    private String passwort;

    public Admin(int adminId, String name, String email,
                 String bereich, String passwort) {
        super(name, email);
        this.adminId  = adminId;
        this.bereich  = bereich;
        this.passwort = passwort;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getBereich() {
        return bereich;
    }

    public String getPasswort() {
        return passwort;
    }

    public void raumVerwalten() {
        System.out.println("Admin " + getName()
                           + " verwaltet Bereich: " + bereich);
    }

    @Override
    public String toString() {
        return "Admin #" + adminId + " | " + getName()
               + " | Bereich: " + bereich;
    }
}
