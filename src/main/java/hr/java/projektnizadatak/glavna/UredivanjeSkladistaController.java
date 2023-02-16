package hr.java.projektnizadatak.glavna;

import hr.java.projektnizadatak.entitet.*;
import hr.java.projektnizadatak.iznimke.BazaPodatakaException;
import hr.java.projektnizadatak.baza.BazaPodataka;
import hr.java.projektnizadatak.iznimke.DatotekaException;
import hr.java.projektnizadatak.iznimke.PromjeneException;
import hr.java.projektnizadatak.threads.AddPromjenaThread;
import hr.java.projektnizadatak.threads.AddPromjeneThread;
import hr.java.projektnizadatak.threads.UpdateTable;
import hr.java.projektnizadatak.util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private  TextField sifraTextField;
    @FXML
    private  TextField robnaMarkaTextField;
    @FXML
    private  TextField kataloskiBrojTextField;
    @FXML
    private  TextField cijenaTextField;
    @FXML
    private  TextField kolicinaTextField;
    @FXML
    private  ChoiceBox<Kategorija> kategorijaChoiceBox;
    @FXML
    private  ChoiceBox <Lokacija> dostavaChoiceBox;
    @FXML
    private  TextField dostavaTextField;

    Long id;
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

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new UpdateTable(artiklTableView));
        executorService.shutdown();

    }



    @FXML
    public void setArtikl(){
        if(!artiklTableView.getSelectionModel().isEmpty()){
            Artikl artikl = artiklTableView.getSelectionModel().getSelectedItem();
            id = artikl.getId();
            sifraTextField.setText(artikl.getSifraProizvoda());
            robnaMarkaTextField.setText(artikl.getRobnaMarkaProizvoda());
            kataloskiBrojTextField.setText(artikl.getKataloskiBrojProizvoda());
            cijenaTextField.setText(artikl.getCijenaProizvoda().toString());
            kolicinaTextField.setText(artikl.getKolicinaProizvoda().toString());
            kategorijaChoiceBox.setValue(artikl.getKategorija());
            dostavaTextField.setText(artikl.getDobavljac().imeDobavljaca());
            dostavaChoiceBox.setValue(artikl.getDobavljac().lokacijaDobavljaca());
        }
    }



    @FXML
    public void update(){
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
            List<String> greskeZaKriviBroj = new ArrayList<>();

            if(sifra.isEmpty())
                greske.add("sifra");
            if(marka.isEmpty())
                greske.add("marka");
            if(kataloskiBroj.isEmpty())
                greske.add("kataloski broj");



            if(ime.isEmpty())
                greske.add("ime dostavljaca");

            if(kategorija == null){
                greske.add("kategorija");
            }

            if(lokacija == null){
                greske.add("lokacija");

            }

            if(cijena.isEmpty())
                greske.add("cijena");
            else {
                cijenaBD = new BigDecimal(cijena);
            }

            if(kolicina.isEmpty())
                greske.add("kolicina");
            else {
                kolicinaINT = Integer.parseInt(kolicina);
                if(kolicinaINT < 0)
                    Glavna.pogresniUnosBroja("negativni broj");
            }


            if(greske.isEmpty()){
                Artikl artikl = artiklTableView.getSelectionModel().getSelectedItem();
                updateArtiklPromjene(artikl);
                BazaPodataka.editArtikl(new Artikl(id, sifra, marka, kataloskiBroj, kategorija , cijenaBD, kolicinaINT, new Dobavljaci(ime, lokacija)));

            }
            else
                Glavna.pogresanUnosPodataka(greske);

        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void spremi() {


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Spremanje");
        alert.setHeaderText("Želite li spremiti podatke o korisniku?");
        ButtonType daButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
        ButtonType neButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(daButton, neButton);
        alert.showAndWait().ifPresent(response -> {
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
                List<String> greskeZaKriviBroj = new ArrayList<>();

                if (sifra.isEmpty())
                    greske.add("sifra");
                if (marka.isEmpty())
                    greske.add("marka");
                if (kataloskiBroj.isEmpty())
                    greske.add("kataloski broj");


                if (ime.isEmpty())
                    greske.add("ime dostavljaca");

                if (kategorija == null) {
                    greske.add("kategorija");
                }

                if (lokacija == null) {
                    greske.add("lokacija");

                }

                if (cijena.isEmpty())
                    greske.add("cijena");
                else
                    cijenaBD = new BigDecimal(cijena);

                if (kolicina.isEmpty())
                    greske.add("kolicina");
                else {
                    kolicinaINT = Integer.parseInt(kolicina);
                    if (kolicinaINT < 0)
                        Glavna.pogresniUnosBroja("negativni broj");
                }


                if (greske.isEmpty()) {
                    BazaPodataka.addArtikl(new Artikl(null, sifra, marka, kataloskiBroj, kategorija, cijenaBD, kolicinaINT, new Dobavljaci(ime, lokacija)));
                    new Thread(new AddPromjenaThread(new Promjena(
                            null,
                            "Dodavanje aritkla",
                            "-",
                            sifraTextField.getText(),
                            Glavna.currentUser.getUsername(),
                            LocalDateTime.now()
                    ))).start();
                }
                else
                    Glavna.pogresanUnosPodataka(greske);



            }
            catch(BazaPodatakaException e){
                throw new RuntimeException(e);
            }

        });




    }

    @FXML
    public void povratak(){
        FXMLLoader fxmlLoader = new FXMLLoader(Glavna.class.getResource("skladiste.fxml"));
        Glavna.prikaziScene(fxmlLoader);
    }


    @FXML
    public void delete(){


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Spremanje");
        alert.setHeaderText("Želite li spremiti podatke o korisniku?");
        ButtonType daButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
        ButtonType neButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(daButton, neButton);
        alert.showAndWait().ifPresent(response -> {
            try {
                Artikl artikl = artiklTableView.getSelectionModel().getSelectedItem();

                new Thread(new AddPromjenaThread(new Promjena(
                        null,
                        "Brisanje",
                        artikl.getSifraProizvoda(),
                        "-",
                        Glavna.currentUser.getUsername(),
                        LocalDateTime.now()
                ))).start();

                BazaPodataka.deleteArtikl(artikl);
                artiklTableView.setItems(FXCollections.observableList(BazaPodataka.getArtikl()));
            } catch (BazaPodatakaException e) {
                throw new RuntimeException(e);
            }
        });




    }






    private void updateArtiklPromjene(Artikl artikl) throws PromjeneException {
        List<String> podatak = new ArrayList<>();
        List<String> staraVrijednost = new ArrayList<>();
        List<String> novaVrijednost = new ArrayList<>();

        if(!sifraTextField.getText().equals(artikl.getSifraProizvoda())){
            podatak.add("Sifra" + artikl.getSifraProizvoda());
            staraVrijednost.add(artikl.getSifraProizvoda());
            novaVrijednost.add(sifraTextField.getText());
        }


        if(!robnaMarkaTextField.getText().equals(artikl.getRobnaMarkaProizvoda())){
            podatak.add("Robna marka od proizvoda: " + artikl.getSifraProizvoda());
            staraVrijednost.add(artikl.getRobnaMarkaProizvoda());
            novaVrijednost.add(robnaMarkaTextField.getText());
        }

        if(!kataloskiBrojTextField.getText().equals(artikl.getKataloskiBrojProizvoda())){
            podatak.add("Kataloski broj od proizvoda: " + artikl.getSifraProizvoda());
            staraVrijednost.add(artikl.getKataloskiBrojProizvoda());
            novaVrijednost.add(kataloskiBrojTextField.getText());
        }
        if(!cijenaTextField.getText().equals(artikl.getCijenaProizvoda())){
            podatak.add("Cijena od proizvoda: " + artikl.getSifraProizvoda());
            staraVrijednost.add(artikl.getCijenaProizvoda().toString());
            novaVrijednost.add(cijenaTextField.getText());
        }

        if(!kolicinaTextField.getText().equals(artikl.getCijenaProizvoda())){
            podatak.add("Kolicina proizvoda: " + artikl.getSifraProizvoda());
            staraVrijednost.add(artikl.getKolicinaProizvoda().toString());
            novaVrijednost.add(kolicinaTextField.getText());
        }

        if(!dostavaTextField.getText().equals(artikl.getDobavljac().imeDobavljaca())){
            podatak.add("Dostavljac proizvoda: " + artikl.getSifraProizvoda());
            staraVrijednost.add(artikl.getDobavljac().imeDobavljaca());
            novaVrijednost.add(dostavaTextField.getText());
        }

        if(!kategorijaChoiceBox.getSelectionModel().getSelectedItem().equals(artikl.getKategorija())){
            podatak.add("Kategorija proizvoda: " + artikl.getSifraProizvoda());
            staraVrijednost.add(artikl.getKategorija().toString());
            novaVrijednost.add(kategorijaChoiceBox.getSelectionModel().getSelectedItem().toString());
        }

        if(!dostavaChoiceBox.getSelectionModel().getSelectedItem().equals(artikl.getKategorija())){
            podatak.add("Drzava dostave proizvoda: " + artikl.getSifraProizvoda());
            staraVrijednost.add(artikl.getDobavljac().lokacijaDobavljaca().toString());
            novaVrijednost.add(dostavaChoiceBox.getSelectionModel().getSelectedItem().toString());
        }



        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy.");


        List<Promjena> promjene = new ArrayList<>(podatak.size());
        for(int i = 0; i < podatak.size(); i++){
            promjene.add(new Promjena(
                    null,
                    podatak.get(i),
                    staraVrijednost.get(i),
                    novaVrijednost.get(i),
                    Glavna.currentUser.getUsername(),
                    LocalDateTime.now()
            ));
        }
        new Thread(new AddPromjeneThread(promjene)).start();
    }
}





