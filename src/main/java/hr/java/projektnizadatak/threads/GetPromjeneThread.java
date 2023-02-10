package hr.java.projektnizadatak.threads;

import hr.java.projektnizadatak.entitet.Promjena;
import hr.java.projektnizadatak.iznimke.PromjeneException;
import hr.java.projektnizadatak.util.Datoteke;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

import java.util.Collections;
import java.util.List;

public class GetPromjeneThread implements Runnable{
    private final List<Promjena> promjene;
    private final TableView<Promjena> tableView;

    public GetPromjeneThread(List<Promjena> promjene, TableView<Promjena> tableView) {
        this.promjene = promjene;
        this.tableView = tableView;
    }
    @Override
    public void run() {
        try {
            List<Promjena> temp = Datoteke.getPromjene();
            Collections.reverse(temp);
            promjene.addAll(temp);
            tableView.setItems(FXCollections.observableList(promjene));
        } catch (PromjeneException e) {
            throw new RuntimeException(e);
        }
    }
}
