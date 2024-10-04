package org.linkedon.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.linkedon.client.Client;
import org.linkedon.client.models.Post;
import org.linkedon.client.models.Profile;
import org.linkedon.client.services.UserService;
import org.linkedon.client.utils.Popup;

import java.io.IOException;
import java.io.InputStream;

public class HomeController {
    @FXML
    private AnchorPane profileBox;
    @FXML
    private Circle avatar;
    @FXML
    private Circle newPostAvatar;
    @FXML
    private HBox newPost;
    @FXML
    private ImageView background;
    @FXML
    private Label name;
    @FXML
    private Label headline;
    @FXML
    private VBox postScene;

    public void initialize() {
        Profile profile = Client.getProfile();
        Rectangle clip = new Rectangle(background.getFitWidth(), background.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        background.setClip(clip);

        name.setText(STR."\{profile.getUser().getFirstName()} \{profile.getUser().getLastName()}");
        headline.setText(profile.getUser().getHeadline());

        UserService userService = UserService.getInstance();
        try {
            InputStream stream = userService.fetchUserHeaderImage(Client.getId(), Client.getToken());
            background.setImage(new Image(stream));
        } catch (Exception e) {
            background.setImage(new Image(Client.class.getResourceAsStream("assets/images/defaultHeader.png")));
        }

        try {
            InputStream stream = userService.fetchUserProfileImage(Client.getId(), Client.getToken());
            Image img = new Image(stream);
            avatar.setFill(new ImagePattern(img));
            newPostAvatar.setFill(new ImagePattern(img));
        } catch (Exception e) {
            Image img = new Image(Client.class.getResourceAsStream("assets/images/defaultProfile.png"));
            avatar.setFill(new ImagePattern(img));
            newPostAvatar.setFill(new ImagePattern(img));
        }

        profileBox.setOnMouseClicked(event -> {
            try {
                MainController.getInstance().showProfile(Client.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        newPost.setOnMouseClicked(event -> {
            try {
                Popup.showPopup("newPost.fxml", new NewPostController(), 700, 470);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        for (Post post : Client.getFeed()) {
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/post.fxml"));
            loader.setController(new PostController(post));
            VBox box;
            try {
                box = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            postScene.getChildren().add(box);
        }
    }
}
