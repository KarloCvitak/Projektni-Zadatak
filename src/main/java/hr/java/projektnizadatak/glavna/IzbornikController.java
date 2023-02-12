package hr.java.projektnizadatak.glavna;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class IzbornikController {


    @FXML
    private VBox vBox;
    @FXML
    private Button skladisteButton;
    @FXML
    private Button promjeneButton;
    @FXML
    private Button profiliButton;
    @FXML
    public void initialize(){

        if(Glavna.currentUser.getRole().equals(1)){

            vBox.getChildren().remove(skladisteButton);
            vBox.getChildren().remove(promjeneButton);
            vBox.getChildren().remove(profiliButton);;
        }


    }
    @FXML
    public void prikaziKorisnike(){
        FXMLLoader fxmlLoader = new FXMLLoader(Glavna.class.getResource("korisnici.fxml"));
        Glavna.prikaziScene(fxmlLoader);
    }

    @FXML
    public void prikazProfila(){
        FXMLLoader fxmlLoader = new FXMLLoader(Glavna.class.getResource("profil.fxml"));
        Glavna.prikaziScene(fxmlLoader);
    }


    @FXML
    public void prikaziPromjene(){
        FXMLLoader fxmlLoader = new FXMLLoader(Glavna.class.getResource("promjene.fxml"));
        Glavna.prikaziScene(fxmlLoader);
    }



    @FXML
    public void prikaziStockManagement(){
        FXMLLoader fxmlLoader = new FXMLLoader(Glavna.class.getResource("skladiste.fxml"));
        Glavna.prikaziScene(fxmlLoader);
    }


    @FXML
    public void prikaziIzradaRacuna(){
        FXMLLoader fxmlLoader = new FXMLLoader(Glavna.class.getResource("izradaracuna.fxml"));
        Glavna.prikaziScene(fxmlLoader);
    }

    @FXML
    public void prikaziLogOut(){
        FXMLLoader fxmlLoader = new FXMLLoader(Glavna.class.getResource("login.fxml"));
        Glavna.prikaziScene(fxmlLoader);
    }



}