package org.linkedon.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.Post;
import org.linkedon.client.models.User;
import org.linkedon.client.services.PostService;
import org.linkedon.client.services.UserService;

import java.io.IOException;
import java.io.InputStream;

public class PostController {
    private Post post;
    @FXML
    private Label name;
    @FXML
    private Label headline;
    @FXML
    private Label text;
    @FXML
    private ImageView postMedia;
    @FXML
    private Circle ownerAvatar;
    @FXML
    private FlowPane like;
    @FXML
    private FontIcon likeIcon;
    @FXML
    private Label likeText;
    @FXML
    private Label likes;

    public PostController(Post post) {
        this.post = post;
    }

    public void initialize() throws ApplicationException, IOException {
        try {
            Image image = new Image(PostService.getInstance().fetchPostMedia(post.getId(), Client.getToken()));
            postMedia.setImage(image);
        } catch (Exception e) {
        }


        User user = UserService.getInstance().fetchUserBasicInfo(post.getUserId(), Client.getToken());
        name.setText(STR."\{user.getFirstName()} \{user.getLastName()}");
        headline.setText(user.getHeadline());
        text.setText(post.getText());
        likes.setText(STR."\{post.getLikes()} Likes");

        if (post.HasLiked()) {
            likeIcon.getStyleClass().add("active");
            likeText.getStyleClass().add("active");
            likeText.setText("Liked");
        }

        ownerAvatar.setOnMouseClicked(_ -> {
            try {
                MainController.getInstance().showProfile(user.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            InputStream stream = UserService.getInstance().fetchUserProfileImage(user.getId(), Client.getToken());
            Image img = new Image(stream);
            ownerAvatar.setFill(new ImagePattern(img));
        } catch (Exception e) {
            Image img = new Image(Client.class.getResourceAsStream("assets/images/defaultProfile.png"));
            ownerAvatar.setFill(new ImagePattern(img));
        }

        like.setOnMouseClicked(event -> {
            if (likeIcon.getStyleClass().contains("active")) return;
            likeIcon.getStyleClass().add("active");
            likeText.getStyleClass().add("active");
            likeText.setText("Liked");

            post.setLikes(post.getLikes() + 1);
            likes.setText(STR."\{post.getLikes()} Likes");

            try {
                PostService.getInstance().addLike(Client.getId(), post.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
