package org.linkedon.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.linkedon.client.Client;
import org.linkedon.client.models.User;
import org.linkedon.client.services.UserService;

import java.io.IOException;
import java.io.InputStream;

public class UserBoxController {
    private User user;
    @FXML
    private Circle avatar;
    @FXML
    private GridPane card;
    @FXML
    private Label nameLabel;
    @FXML
    private Label headlineLabel;

    public UserBoxController(User user) {
        this.user = user;
    }

    public void initialize() {
        try {
            InputStream profileImage = UserService.getInstance().fetchUserProfileImage(user.getId(), Client.getToken());
            avatar.setFill(new ImagePattern(new Image(profileImage)));
        } catch (Exception e) {
            avatar.setFill(new ImagePattern(new Image(Client.class.getResourceAsStream("assets/images/defaultProfile.png"))));
        }

        nameLabel.setText(STR."\{user.getFirstName()} \{user.getLastName()}");
        headlineLabel.setText(user.getHeadline());

        card.setOnMouseClicked(event -> {
            try {
                MainController.getInstance().showProfile(user.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
