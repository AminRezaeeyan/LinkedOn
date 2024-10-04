package org.linkedon.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.User;
import org.linkedon.client.services.NetworkService;
import org.linkedon.client.services.UserService;

import java.io.IOException;
import java.io.InputStream;

public class InviteController {
    private User user;
    @FXML
    private Circle avatar;
    @FXML
    private GridPane invitationCard;
    @FXML
    private Label nameLabel;
    @FXML
    private Label headlineLabel;

    public InviteController(User user) {
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

        invitationCard.setOnMouseClicked(event -> {
            try {
                MainController.getInstance().showProfile(user.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void accept(ActionEvent event) throws ApplicationException, IOException {
        NetworkService.getInstance().accept(Client.getId(), user.getId(), Client.getToken());
        Client.getProfile().getConnections().add(user);
        if (invitationCard.getParent() != null) {
            ((VBox) invitationCard.getParent()).getChildren().remove(invitationCard);
        }
    }

    public void ignore(ActionEvent event) throws ApplicationException, IOException {
        NetworkService.getInstance().reject(Client.getId(), user.getId(), Client.getToken());
        if (invitationCard.getParent() != null) {
            ((VBox) invitationCard.getParent()).getChildren().remove(invitationCard);
        }
    }
}
