package hr.java.projektnizadatak.entitet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class Racun extends Entitet implements Total {

    private List<Artikl> artikli;
    private LocalDateTime localDateTime;
    private Korisnik<Integer> korisnik;


     Racun(Long id, List<Artikl> artikli, LocalDateTime localDateTime, Korisnik<Integer> korisnik) {
        super(id);
        this.artikli = artikli;
        this.localDateTime = localDateTime;
        this.korisnik = korisnik;
    }

    public List<Artikl> getArtikli() {
        return artikli;
    }

    public void setArtikli(List<Artikl> artikli) {
        this.artikli = artikli;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Korisnik<Integer> getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik<Integer> korisnik) {
        this.korisnik = korisnik;
    }

    @Override
    public BigDecimal zbroj(List<Artikl> listaArtikla) {

        BigDecimal sum = listaArtikla.stream()
                .map(Artikl::getCijenaProizvoda)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum;
    }




}
