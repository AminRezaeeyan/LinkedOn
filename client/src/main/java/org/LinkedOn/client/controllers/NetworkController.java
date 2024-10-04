package org.linkedon.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.User;
import org.linkedon.client.services.NetworkService;

import java.io.IOException;
import java.util.ArrayList;

public class NetworkController {
    @FXML
    private Label invitationLabel;
    @FXML
    private VBox inviteContainer;

    @FXML
    private HBox connections;
    @FXML
    private HBox followers;

    public void initialize() {
        ArrayList<User> requests;

        try {
            requests = NetworkService.getInstance().fetchRequests(Client.getId(), Client.getToken());
        } catch (ApplicationException | IOException e) {
            throw new RuntimeException(e);
        }
        if (requests.isEmpty()) invitationLabel.setText("No pending invitations");

        for (User user : requests) {
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/invitation.fxml"));
            loader.setController(new InviteController(user));
            GridPane box;
            try {
                box = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            inviteContainer.getChildren().add(box);
        }

        connections.setOnMouseClicked(event -> {
            try {
                MainController.getInstance().setPage(Client.Page.CONNECTIONS, new ConnectionController(Client.getProfile()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        followers.setOnMouseClicked(event -> {
            try {
                MainController.getInstance().setPage(Client.Page.FOLLOWERS, new FollowController(Client.getProfile()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
