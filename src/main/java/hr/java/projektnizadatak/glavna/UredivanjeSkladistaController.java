package hr.java.projektnizadatak.glavna;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class UredivanjeSkladistaController {



    @FXML
    public void povratak(){
        FXMLLoader fxmlLoader = new FXMLLoader(Glavna.class.getResource("skladiste.fxml"));
        Glavna.prikaziScene(fxmlLoader);
    }



}
