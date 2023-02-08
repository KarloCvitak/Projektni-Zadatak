package hr.java.projektnizadatak.glavna;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;

public class IzbornikController {

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