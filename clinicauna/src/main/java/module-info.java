module Clinicauna {
    // JAVAFX
    requires javafx.controls;
    requires  javafx.fxml;
    requires com.jfoenix;
    requires java.base;
    requires java.logging;
    requires java.desktop;
    // JAKARTA
    requires jakarta.xml.bind;
    requires jakarta.ws.rs;
    requires  jakarta.json;
    // MESSAGES
    requires org.controlsfx.controls;

    opens cr.ac.una.clinicauna to javafx.fxml, javafx.graphics;
    opens cr.ac.una.clinicauna.controller to javafx.fxml, javafx.controls, com.jfoenix;
    opens cr.ac.una.clinicauna.components to javafx.fxml, javafx.controls, com.jfoenix;
    exports cr.ac.una.clinicauna.model;
    exports cr.ac.una.clinicauna.util;
}
