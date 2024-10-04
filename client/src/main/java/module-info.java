module org.linkedon.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;
    requires java.sql;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    exports org.linkedon.client.controllers;

    opens org.linkedon.client to javafx.fxml;
    exports org.linkedon.client;

    opens org.linkedon.client.controllers;
    opens org.linkedon.client.models;
    opens org.linkedon.client.services;
}