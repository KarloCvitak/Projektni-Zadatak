package hr.java.projektnizadatak.glavna;

import hr.java.projektnizadatak.entitet.Artikl;
import hr.java.projektnizadatak.entitet.Korisnik;
import hr.java.projektnizadatak.entitet.Promjena;
import hr.java.projektnizadatak.iznimke.DatotekaException;
import hr.java.projektnizadatak.threads.GetPromjeneThread;
import hr.java.projektnizadatak.util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PromjeneController {

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    @FXML
    private TableView<Promjena> promjeneTableView;
    @FXML
    private TableColumn<Promjena, String > promjenaTableColumn;
    @FXML
    private TableColumn<Promjena, String > staraTableColumn;
    @FXML
    private TableColumn<Promjena, String > novaTableColumn;
    @FXML
    private TableColumn<Promjena, String > userTableColumn;
    @FXML
    private TableColumn<Promjena, String > vrijemeTableColumn;
    private List<Promjena> promjene = new ArrayList<>();
    @FXML
    private ChoiceBox<String> korisnikChoiceBox;
    @FXML
    private ChoiceBox<String> radnjaChoiceBox;

    private List<Korisnik> korisnici;
    private List<Promjena> podatak;

    @FXML
    private ImageView imageView;

    Image image = new Image(Path.of("dat/checkmark.png").toAbsolutePath().toString());


    @FXML
    private void initialize(){

        imageView.setImage(image);

        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                filtriraj();
            }
        });

        try {
            korisnici = Datoteke.getKorisnike();
            podatak = Datoteke.getPromjene();

                korisnikChoiceBox.setItems(FXCollections.observableArrayList(korisnici.stream().map(Korisnik::getUsername).collect(Collectors.toSet())));
                radnjaChoiceBox.setItems(FXCollections.observableArrayList(podatak.stream().map(Promjena::getPodatak).collect(Collectors.toSet())));


        } catch(DatotekaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        promjenaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPodatak()));
        staraTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStaraVrijednost()));
        novaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNovaVrijednost()));
        userTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser()));
        vrijemeTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVrijeme().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"))));

        new Thread(new GetPromjeneThread(promjene, promjeneTableView)).start();



        korisnikChoiceBox.setOnMouseClicked(event -> {
            if (korisnikChoiceBox.getValue() != null) {
                korisnikChoiceBox.setValue(null);
            }
        });

        radnjaChoiceBox.setOnMouseClicked(event -> {
            if (radnjaChoiceBox.getValue() != null) {
                radnjaChoiceBox.setValue(null);
            }
        });
    }



    public void filtriraj(){





      String imeKorisnika = korisnikChoiceBox.getSelectionModel().getSelectedItem();
      String vrstaPodatka = radnjaChoiceBox.getSelectionModel().getSelectedItem();

      List<Promjena> filteredPromjene = promjene;


        if(vrstaPodatka != null)
            filteredPromjene = filteredPromjene.stream().filter(p -> p.getPodatak().contains(vrstaPodatka)).toList();
        if(imeKorisnika != null)
            filteredPromjene = filteredPromjene.stream().filter(p -> p.getUser().contains(imeKorisnika)).toList();


        promjeneTableView.setItems(FXCollections.observableList(filteredPromjene));

    }






}
