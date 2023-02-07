package hr.java.projektnizadatak.glavna;

import hr.java.projektnizadatak.entitet.Artikl;
import hr.java.projektnizadatak.entitet.Dobavljaci;
import hr.java.projektnizadatak.entitet.Kategorija;
import hr.java.projektnizadatak.entitet.Lokacija;
import hr.java.projektnizadatak.iznimke.BazaPodatakaException;
import hr.java.projektnizadatak.util.BazaPodataka;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

    @FXML
    private TextField sifraTextField;
    @FXML
    private TextField robnaMarkaTextField;
    @FXML
    private TextField kataloskiBrojTextField;
    @FXML
    private TextField cijenaTextField;
    @FXML
    private TextField kolicinaTextField;
    @FXML
    private ChoiceBox<Kategorija> kategorijaChoiceBox;
    @FXML
    private ChoiceBox <Lokacija> dostavaChoiceBox;
    @FXML
    private TextField dostavaTextField;

    public void initialize(){
        try {
            artikli = BazaPodataka.getArtikl();

        } catch (BazaPodatakaException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }


        kategorijaChoiceBox.setItems(FXCollections.observableList(Arrays.stream(Kategorija.values()).toList()));
        dostavaChoiceBox.setItems(FXCollections.observableList(Arrays.stream(Lokacija.values()).toList()));


        sifraTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSifraProizvoda()));
        robnaMarkaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRobnaMarkaProizvoda()));
        kataloskiBrojTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKataloskiBrojProizvoda()));
        kategorijaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKategorija().toString()));
        cijenaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCijenaProizvoda().toString()));
        kolicinaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKolicinaProizvoda().toString()));

        artiklTableView.setItems(FXCollections.observableList(artikli));



    }

    @FXML
    public void spremi(){
        try {
            String sifra = sifraTextField.getText();
            String marka = robnaMarkaTextField.getText();
            String kataloskiBroj = kataloskiBrojTextField.getText();


            String cijena = cijenaTextField.getText();
            String kolicina = kolicinaTextField.getText();

            BigDecimal cijenaBD = null;
            Integer kolicinaINT = null;

            Kategorija kategorija = kategorijaChoiceBox.getValue();
            String ime = dostavaTextField.getText();
            Lokacija lokacija = dostavaChoiceBox.getValue();

            List<String> greske = new ArrayList<>();

            if(sifra.isEmpty())
                greske.add("sifra");
            if(marka.isEmpty())
                greske.add("marka");
            if(kataloskiBroj.isEmpty())
                greske.add("kataloski broj");

            if(cijena.isEmpty())
                greske.add("cijena");
            else{
               cijenaBD = BigDecimal.valueOf(Long.parseLong(cijena));

            }

            if(kolicina.isEmpty())
              greske.add("kolicina");
            else {
               kolicinaINT = Integer.parseInt(kolicina);
            }

               if(ime.isEmpty())
              greske.add("ime dostavljaca");

            if(kategorija == null){
                greske.add("kategorija");
            }

            if(lokacija == null){
                greske.add("lokacija");

            }

            if(greske.isEmpty()){
                BazaPodataka.addArtikl(new Artikl(null, sifra, marka, kataloskiBroj, kategorija , cijenaBD, kolicinaINT, new Dobavljaci(ime, lokacija)));
            }
            else
                Glavna.pogresanUnosPodataka(greske);

        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
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
