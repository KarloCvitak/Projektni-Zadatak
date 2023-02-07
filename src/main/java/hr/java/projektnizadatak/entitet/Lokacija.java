package hr.java.projektnizadatak.entitet;



public enum Lokacija {

    SINGAPORE ("SINGAPORE"),
    CHINA ("CHINA"),
    TURKEY ("TURKEY"),
    THAILAND ("THAILAND"),
    YEMEN("YEMEN");

    private final String ime;

    Lokacija( String ime) {
        this.ime = ime;
    }

    @Override
    public String toString() {
        return ime;
    }



}
