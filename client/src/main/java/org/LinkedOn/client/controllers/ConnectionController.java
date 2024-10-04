package org.linkedon.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.linkedon.client.Client;
import org.linkedon.client.models.Profile;
import org.linkedon.client.models.User;

import java.io.IOException;

public class ConnectionController {
    private Profile profile;

    @FXML
    private Label connectionCountLabel;

    @FXML
    private VBox connectionContainer;

    public ConnectionController(Profile profile) {
        this.profile = profile;
    }

    public void initialize() {
        connectionCountLabel.setText(STR."\{profile.getConnections().size()} Connections");
        for (User user : profile.getConnections()) {
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/userBox.fxml"));
            loader.setController(new UserBoxController(user));
            GridPane box;
            try {
                box = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            connectionContainer.getChildren().add(box);
        }
    }

}
