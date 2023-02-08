package hr.java.projektnizadatak.glavna;

import hr.java.projektnizadatak.baza.BazaPodataka;
import hr.java.projektnizadatak.entitet.Artikl;
import hr.java.projektnizadatak.iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class IzradaRacunaController {

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);
    private List<Artikl> artikl;
    @FXML
    private TableColumn<Artikl, String> sifraTableColumn;
    @FXML
    private TableColumn<Artikl, String> robnaMarkaTableColumn;
    @FXML
    private TableColumn<Artikl, String> kataloskiBrojTableColumn;
    @FXML
    private TableColumn<Artikl, String> cijenaTableColumn;
    @FXML
    private TableView artiklTableView;


    @FXML
    public void initialize(){

        try {
            artikl = BazaPodataka.getArtikl();


        } catch (BazaPodatakaException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        sifraTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSifraProizvoda()));
        robnaMarkaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRobnaMarkaProizvoda()));
        kataloskiBrojTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKataloskiBrojProizvoda()));
        cijenaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCijenaProizvoda().toString()));

        artiklTableView.setItems(FXCollections.observableList(artikl));


    }

    @FXML
    public void dodaj(){






    }



}
