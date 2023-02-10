package hr.java.projektnizadatak.util;

import hr.java.projektnizadatak.entitet.Korisnik;
import hr.java.projektnizadatak.entitet.Promjena;
import hr.java.projektnizadatak.glavna.Glavna;
import hr.java.projektnizadatak.iznimke.DatotekaException;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;

public class Datoteke {

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    public static final String USERS_PATH = "dat/users.txt";
    private static final String PROMJENE_PATH = "dat/promjene.dat";

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
        } catch (IOException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new DatotekaException(e);

        }
    }



    public static void addKorisnik(Korisnik korisnik) throws DatotekaException{
        try (BufferedWriter out = new BufferedWriter(new FileWriter(USERS_PATH, true))) {
            OptionalLong optionalId = getKorisnike().stream().mapToLong(p -> p.getId()).max();


              Long id = optionalId.orElse(0) + 1;

                out.write('\n' + id.toString());
                out.write('\n' + korisnik.getUsername());
                out.write('\n' + korisnik.getPassword());
                out.write('\n' + korisnik.getRole().toString());

            Glavna.prikaziScene(new FXMLLoader(Glavna.class.getResource("login.fxml")));
        } catch (IOException e) {
            throw new DatotekaException(e);
        }
    }


    public static void deleteKorisnik(Korisnik korisnik) throws DatotekaException{
        List<String> usersLines;
        try {
            usersLines = Files.lines(Path.of(USERS_PATH)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new DatotekaException(e);
        }

        try(BufferedWriter out = new BufferedWriter(new FileWriter(USERS_PATH, false))){
            for(int i = 0; i < usersLines.size(); i += SIZE_OF_USERS){
                if(Long.parseLong(usersLines.get(i)) == korisnik.getId())
                    continue;

                if(i == 0){
                    out.write(usersLines.get(i) + '\n');
                    out.write(usersLines.get(i + 1) + '\n');
                    out.write(usersLines.get(i + 2) + '\n');
                    out.write(usersLines.get(i + 3));
                    continue;
                }
                out.write('\n' + usersLines.get(i));
                out.write('\n' + usersLines.get(i + 1));
                out.write('\n' + usersLines.get(i + 2));
                out.write('\n' + usersLines.get(i + 3));

            }




        }catch (IOException e){
            throw new DatotekaException(e);
        }
    }

    public static void editKorisnik(Korisnik korisnik) throws DatotekaException{
        try {
            List<String> userLines = Files.lines(Path.of(USERS_PATH)).collect(Collectors.toList());
            for(int i = 0; i < userLines.size(); i += SIZE_OF_USERS)
                if(userLines.get(i).equals(korisnik.getId().toString())){

                    if(korisnik.getUsername() != null)
                        userLines.set(i + 1, korisnik.getUsername());
                    if(korisnik.getPassword() != null)
                        userLines.set(i + 2, korisnik.getPassword());
                    if(korisnik.getRole() != null)
                        userLines.set(i + 3, korisnik.getRole().toString());

                    break;
                }
            String userText = userLines.get(0);
            for(int i = 1; i < userLines.size(); i++)
                userText += '\n' + userLines.get(i);

            Files.write(Path.of(USERS_PATH), userText.getBytes());
        } catch (IOException e) {
            throw new DatotekaException(e);
        }

    }

    //promjene


    public synchronized static List<Promjena> getPromjene() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PROMJENE_PATH))) {
            return (List<Promjena>) in.readObject();

        } catch (ClassNotFoundException | IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public synchronized static void addPromjena(Promjena promjena){
        List<Promjena> promjene = getPromjene();
        promjena.setId((long) (promjene.size() + 1));
        promjene.add(promjena);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PROMJENE_PATH))) {
            out.writeObject(promjene);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException();


        }
    }
    public synchronized static void addPromjene(List<Promjena> novePromjene){
        List<Promjena> promjene = getPromjene();
        for(Promjena promjena: novePromjene){
            promjena.setId((long) (promjene.size() + 1));
            promjene.add(promjena);
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PROMJENE_PATH))) {
            out.writeObject(promjene);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException();
        }
    }



}
