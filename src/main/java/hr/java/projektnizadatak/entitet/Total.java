package hr.java.projektnizadatak.entitet;

import java.math.BigDecimal;
import java.util.List;

public sealed interface Total permits Racun, RacunBuilder{

    BigDecimal zbroj(List<BigDecimal> listaArtikla);



}
