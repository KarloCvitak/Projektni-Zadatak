package hr.java.projektnizadatak.glavna;

import hr.java.projektnizadatak.entitet.Artikl;
import hr.java.projektnizadatak.iznimke.BazaPodatakaException;
import hr.java.projektnizadatak.util.BazaPodataka;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UredivanjeSkladistaController {


    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);
    private List<Artikl> artikli;

    @FXML
    private TableView<Artikl> artiklTableView;
    @FXML
    private TableColumn<Artikl, String> sifraTableColumn;
    @FXML
    private TableColumn<Artikl, String> robnaMarkaTableColumn;
    @FXML
    private TableColumn<Artikl, String> kataloskiBrojTableColumn;
    @FXML
    private TableColumn<Artikl, String> kategorijaTableColumn;
    @FXML
    private TableColumn<Artikl, String> cijenaTableColumn;
    @FXML
    private TableColumn<Artikl, String> kolicinaTableColumn;



    public void initialize(){
        try {
            artikli = BazaPodataka.getArtikl();

        } catch (BazaPodatakaException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        sifraTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSifraProizvoda()));
        robnaMarkaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRobnaMarkaProizvoda()));
        kataloskiBrojTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKataloskiBrojProizvoda()));
        kategorijaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKategorija().toString()));
        cijenaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCijenaProizvoda().toString()));
        kolicinaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKolicinaProizvoda().toString()));

        artiklTableView.setItems(FXCollections.observableList(artikli));



    }



    @FXML
    public void povratak(){
        FXMLLoader fxmlLoader = new FXMLLoader(Glavna.class.getResource("skladiste.fxml"));
        Glavna.prikaziScene(fxmlLoader);
    }


    @FXML
    public void delete(){
        try {
            Artikl artikl = artiklTableView.getSelectionModel().getSelectedItem();
            BazaPodataka.deleteArtikl(artikl);
            artiklTableView.setItems(FXCollections.observableList(BazaPodataka.getArtikl()));
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
    }


}
