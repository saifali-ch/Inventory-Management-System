module Faizi.Traders {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires java.sql;
    requires ojdbc6;
    requires org.controlsfx.controls;
    
    opens main;
    opens controllers;
    opens controllers.alert;
    opens database;
    opens models;
}