package org.linkedon.client.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.Message;
import org.linkedon.client.models.User;
import org.linkedon.client.services.MessageService;
import org.linkedon.client.services.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MessageController {
    private User user;

    @FXML
    private Label name;
    @FXML
    private Circle avatar;
    @FXML
    private VBox chat;
    @FXML
    private TextField input;
    @FXML
    private ScrollPane chatContainer;

    public MessageController(User user) {
        this.user = user;
    }

    public void initialize() throws ApplicationException, IOException {
        name.setText(STR."\{user.getFirstName()} \{user.getLastName()}");
        try {
            InputStream stream = UserService.getInstance().fetchUserProfileImage(user.getId(), Client.getToken());
            avatar.setFill(new ImagePattern(new Image(stream)));
        } catch (Exception e) {
            avatar.setFill(new ImagePattern(new Image(Client.class.getResourceAsStream("assets/images/defaultProfile.png"))));
        }
        ArrayList<Message> messages;
        messages = MessageService.getInstance().getMessages(Client.getId(), user.getId());

        for (Message message : messages){
            Label label = new Label(message.getText());
            label.getStyleClass().add("message");
            label.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            if (message.getSenderId().equals(Client.getId())) label.getStyleClass().add("sender");
            HBox hBox = new HBox(label);
            hBox.setAlignment(message.getSenderId().equals(user.getId()) ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);
            chat.getChildren().add(hBox);
        }

        chat.heightProperty().addListener((observable, oldValue, newValue) -> chatContainer.setVvalue(1.0));

        input.setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.ENTER) return;
            String text = input.getText();
            if ("".equals(text)) return;

            Label label = new Label(text);
            label.getStyleClass().add("message");
            label.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            label.getStyleClass().add("sender");
            HBox hBox = new HBox(label);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            chat.getChildren().add(hBox);
            input.setText("");

            try {
                MessageService.getInstance().sendMessage(new Message(Client.getId(), user.getId(), text));
            } catch (ApplicationException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
