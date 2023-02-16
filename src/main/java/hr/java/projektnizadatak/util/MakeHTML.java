package hr.java.projektnizadatak.util;


import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import hr.java.projektnizadatak.entitet.Artikl;
import hr.java.projektnizadatak.entitet.Racun;
import hr.java.projektnizadatak.entitet.RacunBuilder;
import hr.java.projektnizadatak.glavna.Glavna;
import hr.java.projektnizadatak.iznimke.DatotekaException;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;

public interface MakeHTML {


    public static void racun(Racun racun) throws DatotekaException {


        List <BigDecimal> listaCijena = new ArrayList<>();

        StringBuilder html = new StringBuilder();
        html.append("<html lang=\"hrv\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Document</title>\n <link rel=\"stylesheet\" href=\"artikl.css\">\n <style>.artikl{\n" +
                "\n" +
                "    display:flex;\n" +
                "    justify-content: space-between;\n" +
                "\n" +
                "} </style>" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "        <h1>RACUN id: " + racun.getId() + " </h1>\n" +
                "\n <hr> \n" );

        html.append("    <div class=\"artikl\">  \n" +
                "            <p>Kataloski Broj</p>\n" +
                "            <p>Robna Marka</p>\n" +
                "            <p>Cijena</p>\n" +
                "        </div>");

        for (Artikl artikl : racun.getArtikli() ){
            listaCijena.add(artikl.getCijenaProizvoda());

            html.append("    <div class=\"artikl\">  \n" +
                    "            <p>" + artikl.getKataloskiBrojProizvoda() + "</p>\n" +
                    "            <p>" + artikl.getRobnaMarkaProizvoda() + "</p>\n" +
                    "            <p>" + artikl.getCijenaProizvoda()  + "</p>\n" +
                    "        </div>"
            );




    }
        html.append("</body><hr> <div><p>Total:" + racun.zbroj(listaCijena) + "</p> <p>Racun izradio: " + racun.getKorisnik() + "</p> <hr> <p>Vrijeme izrade: "+ racun.getLocalDateTime() +"</p></div> </html>");



        Path path = Path.of(("dat/racuniHTML/" + racun.getId() + ".html"));

        try {
            Files.writeString(path, html.toString());
          //  System.out.println("BABADOOK " + racun.getId());
        } catch (IOException e){
            throw new DatotekaException();
        }

}


    public static String racunHTML(RacunBuilder racun) throws DatotekaException {
        List <BigDecimal> listaCijena = new ArrayList<>();


        StringBuilder html = new StringBuilder();
        html.append("<html lang=\"hrv\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Document</title>\n <link rel=\"stylesheet\" href=\"artikl.css\">\n <style> .artikl{\n" +
                "\n" +
                "    display:flex;\n" +
                "    justify-content: space-between;\n" +
                "\n" +
                "}  </style>" +


                "</head>\n" +
                "<body>\n" +
                "\n" +
                "        <h1>RACUN id: " + "-" + " </h1>\n" +
                "\n <hr> \n" );

        html.append("    <div class=\"artikl\">  \n" +
                "            <p>Kataloski Broj</p>\n" +
                "            <p>Robna Marka</p>\n" +
                "            <p>Cijena</p>\n" +
                "        </div>");

        for (Artikl artikl : racun.getArtikli() ){
            listaCijena.add(artikl.getCijenaProizvoda());
            html.append("    <div class=\"artikl\">  \n" +
                    "            <p>" + artikl.getKataloskiBrojProizvoda() + "</p>\n" +
                    "            <p>" + artikl.getRobnaMarkaProizvoda() + "</p>\n" +
                    "            <p>" + artikl.getCijenaProizvoda()  + "</p>\n" +
                    "        </div>"
            );

        }

        html.append("</body> <hr> <div><p>Total: " + racun.zbroj(listaCijena) + "</p></div> </html>");


       return html.toString();

    }


}
