module hr.java.vjezbe.glavna {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;

    requires java.logging;
    requires com.jfoenix;
    requires java.sql;


    opens hr.java.projektnizadatak.glavna to javafx.fxml;
    exports hr.java.projektnizadatak.glavna;
}