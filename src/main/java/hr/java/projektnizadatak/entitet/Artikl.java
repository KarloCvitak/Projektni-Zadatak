package hr.java.projektnizadatak.entitet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

//

public class Artikl extends Entitet {

    private String sifraProizvoda;
    private String robnaMarkaProizvoda;
    private String kataloskiBrojProizvoda;
    private Kategorija kategorija;
    private BigDecimal cijenaProizvoda;
    private Integer kolicinaProizvoda;

    private Dobavljaci dobavljac;


    public Artikl(Long id, String sifraProizvoda, String robnaMarkaProizvoda, String kataloskiBrojProizvoda, Kategorija kategorija, BigDecimal cijenaProizvoda, Integer kolicinaProizvoda, Dobavljaci dobavljac) {
        super(id);
        this.sifraProizvoda = sifraProizvoda;
        this.robnaMarkaProizvoda = robnaMarkaProizvoda;
        this.kataloskiBrojProizvoda = kataloskiBrojProizvoda;
        this.kategorija = kategorija;
        this.cijenaProizvoda = cijenaProizvoda;
        this.kolicinaProizvoda = kolicinaProizvoda;
        this.dobavljac = dobavljac;
    }

    public String getSifraProizvoda() {
        return sifraProizvoda;
    }

    public void setSifraProizvoda(String sifraProizvoda) {
        this.sifraProizvoda = sifraProizvoda;
    }

    public String getRobnaMarkaProizvoda() {
        return robnaMarkaProizvoda;
    }

    public void setRobnaMarkaProizvoda(String robnaMarkaProizvoda) {
        this.robnaMarkaProizvoda = robnaMarkaProizvoda;
    }

    public String getKataloskiBrojProizvoda() {
        return kataloskiBrojProizvoda;
    }

    public void setKataloskiBrojProizvoda(String kataloskiBrojProizvoda) {
        this.kataloskiBrojProizvoda = kataloskiBrojProizvoda;
    }

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }

    public BigDecimal getCijenaProizvoda() {
        return cijenaProizvoda;
    }

    public void setCijenaProizvoda(BigDecimal cijenaProizvoda) {
        this.cijenaProizvoda = cijenaProizvoda;
    }

    public Integer getKolicinaProizvoda() {
        return kolicinaProizvoda;
    }

    public void setKolicinaProizvoda(Integer kolicinaProizvoda) {
        this.kolicinaProizvoda = kolicinaProizvoda;
    }

    public Dobavljaci getDobavljac() {
        return dobavljac;
    }

    public void setDobavljac(Dobavljaci dobavljac) {
        this.dobavljac = dobavljac;
    }

    public String toString() {
        return getSifraProizvoda() + " - " + getRobnaMarkaProizvoda() + " - " + getKataloskiBrojProizvoda() + " - " + getCijenaProizvoda();
    }



}
