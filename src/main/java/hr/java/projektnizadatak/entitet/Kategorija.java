package hr.java.projektnizadatak.entitet;

public enum Kategorija {

    TELEVIZOR ("televizor"),
    MOBITELI ("mobiteli"),
    BIJELA_TEHNIKA ("bijela tehnika"),
    LAPTOPI ("laptopi"),
    MALI_KUCANSKI("mali kucanski");



    private final String opis;

    Kategorija( String opis) {
        this.opis = opis;
    }

    @Override
    public String toString() {
        return opis;
    }



}
