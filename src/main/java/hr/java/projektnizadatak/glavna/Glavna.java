package hr.java.projektnizadatak.glavna;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

public class Glavna extends Application {

    static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
       mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Glavna.class.getResource("pocetna.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        scene.getStylesheets().add(getClass().getResource("projektnizadatak.css").toExternalForm());
        stage.getIcons().add(new Image(Path.of("dat/icon.png").toAbsolutePath().toString()));
        stage.setTitle("Faktura");
        stage.setScene(scene);
        stage.show();



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