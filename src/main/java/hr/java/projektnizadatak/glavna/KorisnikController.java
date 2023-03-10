package hr.java.projektnizadatak.glavna;

import hr.java.projektnizadatak.entitet.Korisnik;
import hr.java.projektnizadatak.entitet.Promjena;
import hr.java.projektnizadatak.iznimke.DatotekaException;
import hr.java.projektnizadatak.iznimke.KriviInputException;
import hr.java.projektnizadatak.threads.AddPromjenaThread;
import hr.java.projektnizadatak.threads.AddPromjeneThread;
import hr.java.projektnizadatak.util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KorisnikController {
    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    @FXML
    private TableView<Korisnik> korisnikTableView;
    @FXML
    private TableColumn<Korisnik, String> korisnickoImeColumn;
    @FXML
    private TableColumn<Korisnik, String> razinaPravaColumn;

    @FXML
    private TextField korisnickoImeTextField;
    @FXML
    private ChoiceBox<Integer> razinaPravaChoiceBox;
    private List<Korisnik> korisnici;

    private Korisnik selectedKorisnik = null;

    @FXML
    public void initialize(){

        try {
            korisnici = Datoteke.getKorisnike();

            razinaPravaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole().toString()));
            korisnickoImeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
            korisnikTableView.setItems(FXCollections.observableList(korisnici));

            List<Integer> razinePrava = new ArrayList<>(List.of(0, 1));
            razinaPravaChoiceBox.setItems(FXCollections.observableList(razinePrava));


        }catch(DatotekaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        razinaPravaChoiceBox.setOnMouseClicked(event -> {
            if (razinaPravaChoiceBox.getValue() != null) {
                razinaPravaChoiceBox.setValue(null);
            }
        });


    }

    @FXML
    private void setKorisnik(){

        Korisnik korisnik = korisnikTableView.getSelectionModel().getSelectedItem();

        if(korisnik != null)
        {
            korisnickoImeTextField.setText(korisnik.getUsername());
            razinaPravaChoiceBox.setValue(korisnik.getRole());
        }

    }

    @FXML
    private void filter(){
        String korisnickoIme = korisnickoImeTextField.getText();

        Integer razinaPrava = razinaPravaChoiceBox.getValue();
        List<Korisnik> filteredUsers = korisnici;

        if(!korisnickoIme.isEmpty())
            filteredUsers = filteredUsers.stream().filter(user -> user.getUsername().toLowerCase().contains(korisnickoIme.toLowerCase())).toList();
        if(razinaPrava != null)
            filteredUsers = filteredUsers.stream().filter(user -> user.getRole().equals(razinaPrava)).toList();

        korisnikTableView.setItems(FXCollections.observableList(filteredUsers));


    }



    @FXML
    private void obrisi(){

        selectedKorisnik = korisnikTableView.getSelectionModel().getSelectedItem();


        if(selectedKorisnik != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Brisanje");
            alert.setHeaderText("??elite li obrisati korisnika?");

            ButtonType daButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType neButton = new ButtonType("No", ButtonBar.ButtonData.NO);

            alert.getButtonTypes().setAll(daButton, neButton);
            alert.showAndWait().ifPresent(response -> {
                try {
                    if (response == daButton) {
                        Datoteke.deleteKorisnik(selectedKorisnik);

                        new Thread( new AddPromjenaThread(new Promjena(
                                null,
                                "Obri??i korisnika",
                                selectedKorisnik.getUsername(),
                                "OBRISANO",
                                Glavna.currentUser.getUsername(),
                                LocalDateTime.now()
                        ))).start();


                    }
                } catch (DatotekaException e){
                    logger.error(e.getMessage(), e);
                    e.printStackTrace();
                }
            });
        }
    }



  //  @FXML

    public void spremi() throws DatotekaException {
        try {
            selectedKorisnik = korisnikTableView.getSelectionModel().getSelectedItem();
            String korisnickoIme = korisnickoImeTextField.getText();
            Integer razinaPrava = razinaPravaChoiceBox.getValue();
            List<String> greske = new ArrayList<>();

            if (korisnickoIme.isEmpty())
                greske.add("korisnicko ime");
            if (razinaPrava == null)
                greske.add("razina prava");

            if (!greske.isEmpty()) {
                Glavna.pogresanUnosPodataka(greske);
                return;
            }
            List<Korisnik> sameUser = korisnici.stream().filter(u -> (u.getUsername().equals(korisnickoIme))).toList();

            if (!(sameUser.isEmpty() || korisnickoIme.equals(selectedKorisnik.getUsername()))) {
                logger.warn("To korisnicko ime se vec koristi", new KriviInputException("To korisnicko ime se vec koristi"));
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogre??an unos podataka");
                alert.setHeaderText("Korisnicko ime ili email se vec koristi");
                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Spremanje");
            alert.setHeaderText("??elite li spremiti podatke o korisniku?");
            ButtonType daButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
            ButtonType neButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(daButton, neButton);
            alert.showAndWait().ifPresent(response -> {
                try {
                    if (response == daButton){
                        List<Promjena> promjene = new ArrayList<>();
                        if(!korisnickoIme.equals(selectedKorisnik.getUsername()))
                            promjene.add(new Promjena(
                                    null,
                                    "Korisni??ko ime od " + selectedKorisnik.getUsername(),
                                    selectedKorisnik.getUsername(),
                                    korisnickoIme,
                                    Glavna.currentUser.getUsername(),
                                    LocalDateTime.now()
                            ));

                        if(!razinaPrava.equals(selectedKorisnik.getRole()))
                            promjene.add(new Promjena(
                                    null,
                                    "Razina prava od " + selectedKorisnik.getUsername(),
                                    selectedKorisnik.getRole().toString(),
                                    razinaPrava.toString(),
                                    Glavna.currentUser.getUsername(),
                                    LocalDateTime.now()
                            ));

                        if(!promjene.isEmpty())
                            new Thread(new AddPromjeneThread(promjene)).start();

                        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(32,64,1,15*1024,2);

                        selectedKorisnik.setUsername(korisnickoIme);
                        selectedKorisnik.setRole(razinaPrava);
                        Datoteke.editKorisnik(selectedKorisnik);

                    }
                } catch (DatotekaException e){
                    logger.error(e.getMessage(), e);
                    e.printStackTrace();
                }
            });

        } catch (KriviInputException e){
            logger.warn(e.getMessage(), e);
        }


        korisnikTableView.setItems(FXCollections.observableList(Datoteke.getKorisnike()));


    }



}
