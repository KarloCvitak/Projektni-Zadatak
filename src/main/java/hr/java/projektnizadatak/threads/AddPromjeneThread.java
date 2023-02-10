package hr.java.projektnizadatak.threads;

import hr.java.projektnizadatak.entitet.Promjena;
import hr.java.projektnizadatak.iznimke.PromjeneException;
import hr.java.projektnizadatak.util.Datoteke;

import java.util.List;

public class AddPromjeneThread implements Runnable{
    private final List<Promjena> promjene;

    public AddPromjeneThread(List<Promjena> promjene) {
        this.promjene = promjene;
    }
    @Override
    public void run() {
        try {
            Datoteke.addPromjene(promjene);
        } catch (PromjeneException e) {
            throw new RuntimeException(e);
        }
    }
}
