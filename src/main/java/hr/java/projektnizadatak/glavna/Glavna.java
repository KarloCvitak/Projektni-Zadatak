package hr.java.projektnizadatak.glavna;

import hr.java.projektnizadatak.entitet.Korisnik;
import hr.java.projektnizadatak.entitet.Promjena;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Glavna extends Application {

    static Stage mainStage;
    static Korisnik currentUser = null;

    private static final String PROMJENE_PATH = "dat/promjene.dat";

    @Override
    public void start(Stage stage) throws IOException {


       mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Glavna.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        scene.getStylesheets().add(getClass().getResource("projektnizadatak.css").toExternalForm());
        stage.getIcons().add(new Image(Path.of("dat/icon.png").toAbsolutePath().toString()));
        stage.setTitle("Faktura");
        stage.setScene(scene);
        stage.show();



    }


    public static void pogresanUnosPodataka(List<String> podaci){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pogrešan unos podataka");
        alert.setHeaderText("Molimo ispravite sljedeće pogreške:");

        String greska = podaci.get(0);
        for(int i = 1; i < podaci.size(); i++)
            greska += ", " + podaci.get(i);
        greska = greska.substring(0, 1).toUpperCase() + greska.substring(1);
        if(podaci.size() == 1)
            alert.setContentText(greska + " je obvezan podatak!");
        else
            alert.setContentText(greska + " su obavezni podaci!");

        alert.showAndWait();
    }

    public static void pogresniUnosBroja(String string){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pogrešan unos podataka");
            alert.setContentText("Krivi upis: negativan broj!");

        alert.showAndWait();
    }


    public static void prikaziScene(FXMLLoader fxmlLoader){
        try {
            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
            scene.getStylesheets().add(Glavna.class.getResource("projektnizadatak.css").toExternalForm());
            mainStage.setScene(scene);
            mainStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}