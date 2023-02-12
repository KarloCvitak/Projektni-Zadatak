package hr.java.projektnizadatak.glavna;

import hr.java.projektnizadatak.entitet.Promjena;
import hr.java.projektnizadatak.iznimke.DatotekaException;
import hr.java.projektnizadatak.iznimke.KriviInputException;
import hr.java.projektnizadatak.threads.AddPromjenaThread;
import hr.java.projektnizadatak.util.Datoteke;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.time.LocalDateTime;

public class ProfilController {



    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);


    @FXML
    private TextArea rolaTextArea;
    @FXML
    private TextField imeTextField;
    @FXML
    private PasswordField passPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    public void initialize(){

        rolaTextArea.setText(Glavna.currentUser.getRole().toString());
        imeTextField.setText(Glavna.currentUser.getUsername());


    }



    public void spremi(){
        try {
            String ime = imeTextField.getText();
            String password = passPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (!(password.isEmpty())){

            if (password.length() < 6) {
                logger.warn("Duzina lozinke mora biti barem 6", new KriviInputException("Duzina lozinke mora biti barem 6"));
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogrešan unos podataka");
                alert.setHeaderText("Duzina lozinke mora biti barem 6");
                alert.showAndWait();
                return;
            }
                if (!password.equals(confirmPassword)) {
                    logger.warn("Lozinke se moraju podudarati", new KriviInputException("Lozinke se moraju podudarati"));
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Pogrešan unos podataka");
                    alert.setHeaderText("Lozinke se moraju podudarati");
                    alert.showAndWait();
                    return;
                }

                Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(32,64,1,15*1024,2);

                new Thread( new AddPromjenaThread(new Promjena(
                        null,
                        "Promjena korisnik",
                        Glavna.currentUser.getUsername(),
                        imeTextField.getText(),
                        Glavna.currentUser.getUsername(),
                        LocalDateTime.now()
                ))).start();

                Glavna.currentUser.setPassword(encoder.encode(password));

            }





            Glavna.currentUser.setUsername(ime);

            Datoteke.editKorisnik(Glavna.currentUser);




        } catch (DatotekaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }


}
