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

public class FollowController {
    private Profile profile;

    @FXML
    private Label followerCountLabel;

    @FXML
    private VBox followerContainer;

    public FollowController(Profile profile) {
        this.profile = profile;
    }

    public void initialize() {
        followerCountLabel.setText(STR."\{profile.getFollowers().size()} Followers");
        for (User user : profile.getFollowers()) {
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/userBox.fxml"));
            loader.setController(new UserBoxController(user));
            GridPane box;
            try {
                box = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            followerContainer.getChildren().add(box);
        }
    }

}
