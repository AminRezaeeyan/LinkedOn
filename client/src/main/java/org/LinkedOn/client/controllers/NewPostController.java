package org.linkedon.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.javafx.FontIcon;
import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.Post;
import org.linkedon.client.services.PostService;
import org.linkedon.client.utils.Popup;

import java.io.File;
import java.io.IOException;

public class NewPostController {
    @FXML
    private FontIcon exitPopUp;
    @FXML
    private TextArea textInput;
    @FXML
    private FontIcon mediaInput;

    private String text;
    private File media;

    public void initialize() {
        exitPopUp.setOnMouseClicked(event -> Popup.hidePopup());
        mediaInput.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Post Media");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PNG Files", "*.png")
            );

            File selectedFile = fileChooser.showOpenDialog(Client.getStage());
            if (selectedFile != null) media = selectedFile;
        });
    }

    public void submit() {
        text = textInput.getText();
        if ("".equals(text)) return;

        Post post = new Post();
        post.setText(text);
        post.setUserId(Client.getId());
        post.setLikes(0);

        try {
            post.setId(PostService.getInstance().addPost(post, Client.getToken()));
            if (media != null) PostService.getInstance().addPostMedia(post.getId(), Client.getToken(), media);
        } catch (ApplicationException | IOException e) {
            throw new RuntimeException(e);
        }

        Popup.hidePopup();
    }
}
