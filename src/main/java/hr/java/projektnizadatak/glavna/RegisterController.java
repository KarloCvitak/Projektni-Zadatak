package hr.java.projektnizadatak.glavna;

import hr.java.projektnizadatak.entitet.Korisnik;
import hr.java.projektnizadatak.entitet.Promjena;
import hr.java.projektnizadatak.iznimke.DatotekaException;
import hr.java.projektnizadatak.iznimke.KriviInputException;
import hr.java.projektnizadatak.threads.AddPromjenaThread;
import hr.java.projektnizadatak.util.Datoteke;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordConfirmField;

    @FXML
    private ImageView imageView;

    Image image = new Image(Path.of("dat/return.png").toAbsolutePath().toString());

    @FXML
    public void initialize(){

        imageView.setImage(image);

        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                backToLogin();
            }
        });


    }

    @FXML
    public void register(){
        try {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            String passwordConfirm = passwordConfirmField.getText();
            List<Korisnik> korisnici = Datoteke.getKorisnike();
            List<String> greske = new ArrayList<>();


            if (username.isEmpty())
                greske.add("username");
            if (password.isEmpty())
                greske.add("password");
            if (passwordConfirm.isEmpty())
                greske.add("confirm password");

            if (!greske.isEmpty()) {
                Glavna.pogresanUnosPodataka(greske);
                return;
            }
            List<Korisnik> korisnik = korisnici.stream().filter(u -> (u.getUsername().equals(username))).toList();
            if (!korisnik.isEmpty()) {
                logger.warn("To korisnicko ime vec koristi", new KriviInputException("To korisnicko ime se vec koristi"));
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogrešan unos podataka");
                alert.setHeaderText("Korisnicko ime se vec koristi");
                alert.showAndWait();
                return;
            }
            if (password.length() < 6) {
                logger.warn("Duzina lozinke mora biti barem 6", new KriviInputException("Duzina lozinke mora biti barem 6"));
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogrešan unos podataka");
                alert.setHeaderText("Duzina lozinke mora biti barem 6");
                alert.showAndWait();
                return;
            }
            if (!password.equals(passwordConfirm)) {
                logger.warn("Lozinke se moraju podudarati", new KriviInputException("Lozinke se moraju podudarati"));
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogrešan unos podataka");
                alert.setHeaderText("Lozinke se moraju podudarati");
                alert.showAndWait();
                return;
            }

            Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(32,64,1,15*1024,2);

            Datoteke.addKorisnik(new Korisnik(null, username, encoder.encode(password), 1));
            backToLogin();

            new Thread( new AddPromjenaThread(new Promjena(
                    null,
                    "Dodan korisnik",
                    "-",
                    username,
                    username,
                    LocalDateTime.now()
            ))).start();


        } catch (DatotekaException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (KriviInputException e){
            logger.warn(e.getMessage(), e);
        }
    }

    @FXML
    public void backToLogin(){
        Glavna.prikaziScene(new FXMLLoader(Glavna.class.getResource("login.fxml")));
    }
}
