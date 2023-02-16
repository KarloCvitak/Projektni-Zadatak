package hr.java.projektnizadatak.entitet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class RacunBuilder implements Total {
    private Long id;
    private List<Artikl> artikli;
    private LocalDateTime localDateTime;
    private Korisnik<Integer> korisnik;


    public List<Artikl> getArtikli() {
        return artikli;
    }

    public RacunBuilder(){
        artikli = new ArrayList<>();
    }

    public RacunBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public RacunBuilder setArtikli(List<Artikl> artikli) {
        this.artikli = artikli;
        return this;
    }

    public RacunBuilder setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
        return this;
    }

    public RacunBuilder setKorisnik(Korisnik<Integer> korisnik) {
        this.korisnik = korisnik;
        return this;
    }

    public Racun createRacun() {
        return new Racun(id, artikli, localDateTime, korisnik);
    }


    public void addArtikl(Artikl artikl) {
        this.artikli.add(artikl);
    }

    @Override
    public BigDecimal zbroj(List<BigDecimal> listaArtikla) {

        BigDecimal sum = listaArtikla.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum;
    }

}