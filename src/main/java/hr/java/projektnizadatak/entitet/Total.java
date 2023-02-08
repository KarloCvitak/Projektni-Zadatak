package hr.java.projektnizadatak.entitet;

import java.math.BigDecimal;
import java.util.List;

public sealed interface Total permits Racun{
    BigDecimal zbroj(List<Artikl> listaArtikla);
}
