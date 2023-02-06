package hr.java.projektnizadatak.util;

import hr.java.projektnizadatak.entitet.Artikl;
import hr.java.projektnizadatak.entitet.Dobavljaci;
import hr.java.projektnizadatak.entitet.Kategorija;
import hr.java.projektnizadatak.iznimke.*;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BazaPodataka {


    private static final String DATABASE_FILE = "database.properties";



    private static Connection spajanjeNaBazu() throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(DATABASE_FILE));
        String dataBaseUrl = properties.getProperty("dataBaseUrl");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        return DriverManager.getConnection(dataBaseUrl, username, password);
    }

    public static List<Artikl> getArtikl() throws BazaPodatakaException {
        List<Artikl> artikli = new ArrayList<>();

        try(Connection connection = spajanjeNaBazu()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM ARTIKL");

            while (rs.next()){
                Long id = rs.getLong("ID");
                String sifraProizvoda = rs.getString("SIFRA");
                String robnaMarkaProizvoda = rs.getString("MARKA");
                String katoloskiBrojProizvoda = rs.getString("KATALOSKI_BROJ");
                String kategorija = rs.getString("KATEGORIJA");
                BigDecimal cijenaProizvoda = rs.getBigDecimal("CIJENA");
                Integer kolicinaProizvoda = rs.getInt("KOLICINA");

                String ime = rs.getString("IME_DOBAVLJACA");
                String lokacija = rs.getString("LOKACIJA");



                artikli.add(new Artikl(id, sifraProizvoda, robnaMarkaProizvoda, katoloskiBrojProizvoda, Kategorija.valueOf(kategorija) , cijenaProizvoda, kolicinaProizvoda, new Dobavljaci(ime, lokacija)));
            }
        } catch (SQLException | IOException e) {
            throw new BazaPodatakaException(e);
        }

        return artikli;
    }









}
