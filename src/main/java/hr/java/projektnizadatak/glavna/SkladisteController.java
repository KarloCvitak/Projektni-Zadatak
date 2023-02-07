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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private  ChoiceBox<String> markaChoiceBox;
    @FXML
    private ChoiceBox<Kategorija> kategorijaChoiceBox;

    @FXML
    private TextArea prikazDobavljaca;


    private final ToggleGroup dostupnost = new ToggleGroup();

    public void initialize(){
        try {
            artikli = BazaPodataka.getArtikl();
            kategorijaChoiceBox.setItems(FXCollections.observableList(Arrays.stream(Kategorija.values()).toList()));
            markaChoiceBox.setItems(FXCollections.observableArrayList(artikli.stream().map(Artikl::getRobnaMarkaProizvoda).collect(Collectors.toSet())));



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


        artiklTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                prikazDobavljaca.setText(newSelection.getDobavljac().imeDobavljaca() + "\n" + newSelection.getDobavljac().lokacijaDobavljaca());
            }
        });
    }


    @FXML
    public void prikaziUredivanjeSkladista(){
        FXMLLoader fxmlLoader = new FXMLLoader(Glavna.class.getResource("dodavanjeartikla.fxml"));
        Glavna.prikaziScene(fxmlLoader);
    }

   public void setPrikazDobavljaca(){



   }


    public void pretraziSkladiste(){

        String sifra = sifraTextField.getText();
        Integer kolicinaProizvoda = null;

        Kategorija kategorija = kategorijaChoiceBox.getValue();
        String marka = markaChoiceBox.getValue();

        boolean dostupno = dostupnoRadioButton.isSelected();
        boolean nedostupno = nedostupnoRadioButton.isSelected();


        if(nedostupno){
            kolicinaProizvoda = 0;
        }
        if(dostupno){
            kolicinaProizvoda = -1;
        }

        if(sifra.isEmpty())
            sifra = null;





        try {
            artiklTableView.setItems(FXCollections.observableList(BazaPodataka.getFilteredArtikl(new Artikl(null, sifra, marka, null, kategorija, null, kolicinaProizvoda, new Dobavljaci(null,null)))));
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }


    }


}


/*
*    if(nedostupno){
            kolicinaProizvoda = 0;
        }
        if (dostupno){
            kolicinaProizvoda = null;
        }

*
* */