package hr.java.projektnizadatak.entitet;

public enum Kategorija {

    TELEVIZOR ("TELEVIZOR"),
    MOBITELI ("MOBITELI"),
    BIJELA_TEHNIKA ("BIJELA_TEHNIKA"),
    LAPTOPI ("LAPTOPI"),
    MALI_KUCANSKI("MALI_KUCANSKI");



    private final String opis;

    Kategorija( String opis) {
        this.opis = opis;
    }

    @Override
    public String toString() {
        return opis;
    }



}
