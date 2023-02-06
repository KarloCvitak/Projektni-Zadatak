package hr.java.projektnizadatak.glavna;

import hr.java.projektnizadatak.entitet.Dobavljaci;
import hr.java.projektnizadatak.entitet.Kategorija;
import hr.java.projektnizadatak.iznimke.BazaPodatakaException;
import hr.java.projektnizadatak.entitet.Artikl;
import hr.java.projektnizadatak.util.BazaPodataka;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SkladisteController {

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

    @FXML
    private TextField sifraTextField;
    @FXML
    private RadioButton dostupnoRadioButton;
    @FXML
    private RadioButton nedostupnoRadioButton;

    @FXML
    private  ChoiceBox<Artikl> markaChoiceBox;
    @FXML
    private ChoiceBox<Artikl> kategorijaChoicebox;




    private final ToggleGroup dostupnost = new ToggleGroup();

    public void initialize(){
        try {
            artikli = BazaPodataka.getArtikl();
           //  markaChoiceBox.setItems(FXCollections.observableList(BazaPodataka.get);
           // kategorijaChoicebox.setItems(FXCollections.observableList(Kategorija));


        } catch (BazaPodatakaException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        dostupnoRadioButton.setToggleGroup(dostupnost);
        nedostupnoRadioButton.setToggleGroup(dostupnost);

        sifraTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSifraProizvoda()));
        robnaMarkaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRobnaMarkaProizvoda()));
        kataloskiBrojTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKataloskiBrojProizvoda()));
        kategorijaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKategorija().toString()));
        cijenaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCijenaProizvoda().toString()));
        kolicinaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKolicinaProizvoda().toString()));

        artiklTableView.setItems(FXCollections.observableList(artikli));



    }


    @FXML
    public void prikaziUredivanjeSkladista(){
        FXMLLoader fxmlLoader = new FXMLLoader(Glavna.class.getResource("dodavanjeartikla.fxml"));
        Glavna.prikaziScene(fxmlLoader);
    }

    public void pretraziSkladiste(){

        String sifra = sifraTextField.getText();
        Integer kolicinaProizvoda = null;

        if(sifra.isEmpty())
            sifra = null;


        Boolean dostupno = dostupnoRadioButton.isSelected();
        Boolean nedostupno = nedostupnoRadioButton.isSelected();


        if (dostupno){
        kolicinaProizvoda = 15;
        } else{
          kolicinaProizvoda = 0;
        }


        try {
            artiklTableView.setItems(FXCollections.observableList(BazaPodataka.getFilteredArtikl(new Artikl(null, sifra, null, null, null, null, kolicinaProizvoda, new Dobavljaci(null,null)))));
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }


    }


}
