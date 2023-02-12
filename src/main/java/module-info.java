module hr.java.vjezbe.glavna {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires spring.security.crypto;
    requires java.logging;
    requires com.jfoenix;
    requires java.sql;
    requires spire.pdf;
    requires javafx.web;
    requires itextpdf;
    requires pdfrender;
    requires java.desktop;

    opens hr.java.projektnizadatak.glavna to javafx.fxml;
    exports hr.java.projektnizadatak.glavna;
}