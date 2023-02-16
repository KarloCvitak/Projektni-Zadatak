package hr.java.projektnizadatak.glavna;

import com.itextpdf.pdfrender.PdfRenderImageType;
import com.itextpdf.pdfrender.PdfToImageRenderer;
import com.itextpdf.pdfrender.RenderingProperties;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;



import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import hr.java.projektnizadatak.util.MakeHTML;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.control.*;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import hr.java.projektnizadatak.baza.BazaPodataka;
import hr.java.projektnizadatak.entitet.Artikl;
import hr.java.projektnizadatak.entitet.Racun;
import hr.java.projektnizadatak.entitet.RacunBuilder;
import hr.java.projektnizadatak.iznimke.BazaPodatakaException;
import hr.java.projektnizadatak.iznimke.DatotekaException;
import hr.java.projektnizadatak.util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.print.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
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
    private TableView<Artikl> artiklTableView;
    private Artikl selectedArtikl;

    @FXML
    private ListView<Artikl> dodaniArtikli;
    @FXML
    private WebView webView;
    @FXML
    private Button btn;

    private RacunBuilder racun;
    WebEngine webEngine;

    @FXML
    public void initialize() {

        try {
            artikl = BazaPodataka.getArtikl();


        } catch (BazaPodatakaException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        racun = new RacunBuilder();
        racun.setKorisnik(Glavna.currentUser);



         webEngine = webView.getEngine();


       // File htmlFile = new File("dat/racuniHTML/1.html");
        // webEngine.load(htmlFile.toURI().toString());



        sifraTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSifraProizvoda()));
        robnaMarkaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRobnaMarkaProizvoda()));
        kataloskiBrojTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKataloskiBrojProizvoda()));
        cijenaTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCijenaProizvoda().toString()));
        artiklTableView.setItems(FXCollections.observableList(artikl));

        btn.setOnAction((ActionEvent e) -> {
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {
                if (job.showPrintDialog(webView.getScene().getWindow())) {
                    PageLayout pageLayout = job.getJobSettings().getPageLayout();
                    job.getJobSettings().setPageLayout(pageLayout);

                     webEngine.print(job);
                        job.endJob();
            }
        }}
            );


    }




    @FXML
    public void dodaj() {


        Artikl artikl = artiklTableView.getSelectionModel().getSelectedItem();
        if (artikl.getKolicinaProizvoda() > 0) {
            artikl.setKolicinaProizvoda(artikl.getKolicinaProizvoda() - 1);

            racun.addArtikl(artikl);

            try {

                webEngine.loadContent(MakeHTML.racunHTML(racun));

            } catch (DatotekaException e){
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }



            try {
                BazaPodataka.editArtikl(artikl);

            } catch (BazaPodatakaException e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }



        } else{
            Glavna.krivaVrijednostBroja();
        }





        dodaniArtikli.setItems(FXCollections.observableList(racun.getArtikli()));
    }




    @FXML
    public void delete(){

        Artikl artikl = dodaniArtikli.getSelectionModel().getSelectedItem();

        if (artikl != null){
            racun.getArtikli().remove(artikl);
            artikl.setKolicinaProizvoda(artikl.getKolicinaProizvoda() + 1);
            dodaniArtikli.setItems(FXCollections.observableList(racun.getArtikli()));
            try {
                BazaPodataka.editArtikl(artikl);
                webEngine.loadContent(MakeHTML.racunHTML(racun));


            } catch (BazaPodatakaException | DatotekaException e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }

        }


    }

    @FXML
    public void spremi() {


        racun.setLocalDateTime(LocalDateTime.now());



        try {
            Racun racuni = racun.createRacun();
            Datoteke.spremiRacun(racuni);
            racuni.setId(Datoteke.getRacune().stream().mapToLong(Racun::getId).max().orElse(0L));

            MakeHTML.racun(racuni);
          //  System.out.println(idRacuna.size());


            webEngine.loadContent(Files.readString(Path.of("dat/racuniHTML/" + racuni.getId().toString() + ".html")));




        } catch (DatotekaException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        racun = new RacunBuilder();
        racun.setKorisnik(Glavna.currentUser);
        dodaniArtikli.setItems(FXCollections.observableList(new ArrayList<>()));


    }


}