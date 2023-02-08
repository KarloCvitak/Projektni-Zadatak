package hr.java.projektnizadatak.threads;

import hr.java.projektnizadatak.baza.BazaPodataka;
import hr.java.projektnizadatak.entitet.Artikl;
import hr.java.projektnizadatak.iznimke.BazaPodatakaException;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;


public class UpdateTable implements Runnable{
    private TableView<Artikl> artiklTableView;

    public UpdateTable(TableView<Artikl> artiklTableView) {
        this.artiklTableView = artiklTableView;
    }

    @Override
    public void run() {
        while (true){
            try {
                artiklTableView.setItems(FXCollections.observableList(BazaPodataka.getArtikl()));
                Thread.sleep(5555);
            } catch (BazaPodatakaException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
