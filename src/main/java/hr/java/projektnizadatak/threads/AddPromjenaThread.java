package hr.java.projektnizadatak.threads;

import hr.java.projektnizadatak.entitet.Promjena;
import hr.java.projektnizadatak.iznimke.PromjeneException;
import hr.java.projektnizadatak.util.Datoteke;

public class AddPromjenaThread implements Runnable{
    private final Promjena promjena;

    public AddPromjenaThread(Promjena promjena) {
        this.promjena = promjena;
    }
    @Override
    public void run() {
        try {
            Datoteke.addPromjena(promjena);
        } catch (PromjeneException e) {
            throw new RuntimeException(e);
        }
    }
}
