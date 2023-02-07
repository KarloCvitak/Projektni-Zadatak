package hr.java.projektnizadatak.util;

import hr.java.projektnizadatak.entitet.Artikl;
import hr.java.projektnizadatak.entitet.Dobavljaci;
import hr.java.projektnizadatak.entitet.Kategorija;
import hr.java.projektnizadatak.entitet.Lokacija;
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


    public static void deleteArtikl(Artikl Artikl) throws BazaPodatakaException{
        try(Connection connection = spajanjeNaBazu()){
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ARTIKL WHERE ID = ?");
            preparedStatement.setLong(1, Artikl.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
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



                artikli.add(new Artikl(id, sifraProizvoda, robnaMarkaProizvoda, katoloskiBrojProizvoda, Kategorija.valueOf(kategorija) , cijenaProizvoda, kolicinaProizvoda, new Dobavljaci(ime, Lokacija.valueOf(lokacija))));
            }
        } catch (SQLException | IOException e) {
            throw new BazaPodatakaException(e);
        }

        return artikli;
    }




    public static List<Artikl> getFilteredArtikl(Artikl artikl) throws BazaPodatakaException {
        try(Connection connection = spajanjeNaBazu()) {
            if(artikl == null){
                return getArtikl();
            }
            else{
                List<Artikl> artikli = new ArrayList<>();

                StringBuilder sqlUpit = new StringBuilder("SELECT * FROM ARTIKL WHERE 1 = 1");

                if(artikl.getId() != null)
                    sqlUpit.append(" AND ID = " + artikl.getId());


                if(artikl.getSifraProizvoda() != null)
                    sqlUpit.append(" AND SIFRA LIKE '%" + artikl.getSifraProizvoda() + "%'");


                if(artikl.getRobnaMarkaProizvoda() != null)
                    sqlUpit.append(" AND MARKA LIKE '%" + artikl.getRobnaMarkaProizvoda() + "%'");


                if(artikl.getKataloskiBrojProizvoda() != null)
                    sqlUpit.append(" AND KATALOSKI_BROJ LIKE '%" + artikl.getKataloskiBrojProizvoda() + "%'");

                if(artikl.getKategorija() != null)
                    sqlUpit.append(" AND KATEGORIJA LIKE '%" + artikl.getKategorija() + "%'");

                if(artikl.getCijenaProizvoda() != null)
                    sqlUpit.append(" AND CIJENA =" + artikl.getCijenaProizvoda());

                if(artikl.getKolicinaProizvoda() != null) {
                    if (artikl.getKolicinaProizvoda() == 0)
                        sqlUpit.append(" AND KOLICINA = " + artikl.getKolicinaProizvoda());
                    if (artikl.getKolicinaProizvoda() == -1)
                        sqlUpit.append(" AND KOLICINA <> 0");

                }


                if(artikl.getDobavljac().imeDobavljaca() != null)
                    sqlUpit.append(" AND IME_DOBAVLJACA LIKE '%" + artikl.getDobavljac().imeDobavljaca() + "%'");

                if(artikl.getDobavljac().lokacijaDobavljaca() != null)
                    sqlUpit.append(" AND LOKACIJA LIKE '%" + artikl.getDobavljac().lokacijaDobavljaca() + "%'");


                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sqlUpit.toString());

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

                    artikli.add(new Artikl(id, sifraProizvoda, robnaMarkaProizvoda, katoloskiBrojProizvoda, Kategorija.valueOf(kategorija) , cijenaProizvoda, kolicinaProizvoda, new Dobavljaci(ime, Lokacija.valueOf(lokacija))));
                }


                return artikli;
            }
        } catch (SQLException | IOException e) {
            throw new BazaPodatakaException(e);
        }
    }


    public static void addArtikl(Artikl artikl) throws BazaPodatakaException{
        try(Connection connection = spajanjeNaBazu()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ARTIKL (SIFRA, MARKA, KATALOSKI_BROJ, KATEGORIJA, CIJENA, KOLICINA, IME_DOBAVLJACA, LOKACIJA ) VALUES (?, ?, ?, ?, ?, ? , ?, ?)");
            preparedStatement.setString(1, artikl.getSifraProizvoda());
            preparedStatement.setString(2, artikl.getRobnaMarkaProizvoda());
            preparedStatement.setString(3, artikl.getKataloskiBrojProizvoda());
            preparedStatement.setString(4, artikl.getKategorija().toString());
            preparedStatement.setBigDecimal(5, artikl.getCijenaProizvoda());
            preparedStatement.setInt(6, artikl.getKolicinaProizvoda());
            preparedStatement.setString(7, artikl.getDobavljac().imeDobavljaca());
            preparedStatement.setString(8, artikl.getDobavljac().lokacijaDobavljaca().toString());

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new BazaPodatakaException(e);
        }
    }




}
