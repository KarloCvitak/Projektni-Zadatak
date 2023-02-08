package hr.java.projektnizadatak.util;

import hr.java.projektnizadatak.entitet.Korisnik;
import hr.java.projektnizadatak.glavna.Glavna;
import hr.java.projektnizadatak.iznimke.DatotekaException;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;

public class Datoteke {

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    public static final String USERS_PATH = "dat/users.txt";
    public static final int SIZE_OF_USERS = 4;

    public static List<Korisnik> getKorisnike() throws DatotekaException {
        try(BufferedReader reader = new BufferedReader(new FileReader(USERS_PATH))) {
            List<String> usersLines = reader.lines().collect(Collectors.toList());
            List<Korisnik> users = new ArrayList<>();
            for(int i = 0; i < usersLines.size(); i += SIZE_OF_USERS){

                users.add(new Korisnik(
                        Long.parseLong(usersLines.get(i)),
                        usersLines.get(i + 1),
                        usersLines.get(i + 2),
                        Integer.parseInt(usersLines.get(i + 3))

                ));
            }
            return users;
        }catch (IOException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new DatotekaException(e);

        }
    }



    public static void addUser(Korisnik korisnik) throws DatotekaException{
        try (BufferedWriter out = new BufferedWriter(new FileWriter(USERS_PATH, true))) {
            OptionalLong optionalId = getKorisnike().stream().mapToLong(p -> p.getId()).max();

            Long id = optionalId.getAsLong() + 1;
            out.write('\n' + id.toString());
            out.write('\n' + korisnik.getUsername());
            out.write('\n' + korisnik.getPassword());
            out.write('\n' + korisnik.getRole().toString());

            Glavna.prikaziScene(new FXMLLoader(Glavna.class.getResource("login.fxml")));
        } catch (IOException e) {
            throw new DatotekaException(e);
        }
    }



}
