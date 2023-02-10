package hr.java.projektnizadatak.glavna;




import hr.java.projektnizadatak.entitet.Korisnik;
import hr.java.projektnizadatak.iznimke.DatotekaException;
import hr.java.projektnizadatak.iznimke.KriviInputException;
import hr.java.projektnizadatak.util.Datoteke;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrijavaController {

    Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(32,64,1,15*1024,2);
    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;



    @FXML
    public void login(){
        try {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            List<Korisnik> korisnici = Datoteke.getKorisnike();
            List<String> greske = new ArrayList<>();


            if (username.isEmpty())
                greske.add("korisnicko ime");
            if (password.isEmpty())
                greske.add("lozinka");


            if (greske.isEmpty()) {
                try {
                    Korisnik korisnik = korisnici.stream().filter(u -> u.getUsername().equals(username)).toList().get(0);
                    if(encoder.matches(password, korisnik.getPassword())){

                        Glavna.currentUser = korisnik;
                        goToPocetna();
                    }
                    else
                        throw new ArrayIndexOutOfBoundsException("Kriva lozinka");

                } catch (ArrayIndexOutOfBoundsException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Pogrešan unos podataka");
                    alert.setHeaderText("Korisnicko ime ili lozinka se ne podudaraju");
                    alert.showAndWait();
                    logger.error(e.getMessage(), e);
                }
            } else

                Glavna.pogresanUnosPodataka(greske);
        } catch (DatotekaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();

        } catch (KriviInputException e){
            logger.warn(e.getMessage(), e);
        }

    }

    @FXML
    public void goToPocetna(){
        Glavna.prikaziScene(new FXMLLoader(Glavna.class.getResource("pocetna.fxml")));
    }

    @FXML
    public void goToRegister(){
        Glavna.prikaziScene(new FXMLLoader(Glavna.class.getResource("register.fxml")));
    }


}
