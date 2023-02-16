package hr.java.projektnizadatak.glavna;

import hr.java.projektnizadatak.entitet.LocalTimeDate;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PocetnaController{

    @FXML
    private Label time;



    @FXML
    public void initialize() {



        LocalTimeDate<LocalDate, LocalTime> localDateTime = new LocalTimeDate<>(LocalDate.now() , LocalTime.now());



        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e ->

                time.setText(localDateTime.getLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")) + localDateTime.getLocalTime().format(DateTimeFormatter.ofPattern("\nHH:mm:ss")))

        ),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();




    }

}