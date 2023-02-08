package hr.java.projektnizadatak.entitet;

import java.math.BigDecimal;
import java.util.List;

public final class Racun extends Artikl implements Total {

    private List<Artikl> listaArtikla;

    public Racun(Long id, String sifraProizvoda, String robnaMarkaProizvoda, String kataloskiBrojProizvoda, Kategorija kategorija, BigDecimal cijenaProizvoda, Integer kolicinaProizvoda, Dobavljaci dobavljac, List<Artikl> listaArtikla) {
        super(id, sifraProizvoda, robnaMarkaProizvoda, kataloskiBrojProizvoda, kategorija, cijenaProizvoda, kolicinaProizvoda, dobavljac);
        this.listaArtikla = listaArtikla;
    }

    @Override
    public BigDecimal zbroj(List<Artikl> listaArtikla) {

        BigDecimal sum = listaArtikla.stream()
                .map(Artikl::getCijenaProizvoda)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum;
    }

}
