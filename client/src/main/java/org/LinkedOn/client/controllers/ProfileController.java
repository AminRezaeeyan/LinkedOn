package org.linkedon.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.javafx.FontIcon;
import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.Education;
import org.linkedon.client.models.Profile;
import org.linkedon.client.models.User;
import org.linkedon.client.services.NetworkService;
import org.linkedon.client.services.UserService;
import org.linkedon.client.utils.Popup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ProfileController {
    private Profile profile;
    private boolean editable = true;
    @FXML
    private Label name;
    @FXML
    private Label headline;
    @FXML
    private Button connections;
    @FXML
    private Label locationLabel;
    @FXML
    private ImageView background;
    @FXML
    private Circle avatar;
    @FXML
    private Label currentEducation;
    @FXML
    private FontIcon editProfile;
    @FXML
    private VBox educations;
    @FXML
    private VBox skills;
    @FXML
    private FontIcon addSkill;
    @FXML
    private FontIcon addEducation;
    @FXML
    private Button connect;
    @FXML
    private Button follow;
    @FXML
    private Button message;

    public ProfileController(Profile profile) {
        this.profile = profile;
    }

    public void initialize() {
        educations.getChildren().clear();
        skills.getChildren().clear();
        Rectangle clip = new Rectangle(background.getFitWidth(), background.getFitHeight());

        clip.setArcWidth(30);
        clip.setArcHeight(30);
        background.setClip(clip);

        name.setText(STR."\{profile.getUser().getFirstName()} \{profile.getUser().getLastName()}");
        if (profile.getUser().getHeadline() != null) headline.setText(profile.getUser().getHeadline());
        locationLabel.setText(profile.getUser().getLocation());
        connections.setText(STR."\{profile.getConnections().size()} Connections");

        UserService userService = UserService.getInstance();
        try {
            InputStream stream = userService.fetchUserHeaderImage(profile.getUser().getId(), Client.getToken());
            background.setImage(new Image(stream));
        } catch (Exception e) {
            background.setImage(new Image(Client.class.getResourceAsStream("assets/images/defaultHeader.png")));
        }

        try {
            InputStream stream = userService.fetchUserProfileImage(profile.getUser().getId(), Client.getToken());
            avatar.setFill(new ImagePattern(new Image(stream)));
        } catch (Exception e) {
            avatar.setFill(new ImagePattern(new Image(Client.class.getResourceAsStream("assets/images/defaultProfile.png"))));
        }

        editProfile.setVisible(false);
        addSkill.setVisible(false);
        addEducation.setVisible(false);
        if (profile.getUser().getId().equals(Client.getId())) {
            initializeEdit();
            editable = true;
        }


        if (!profile.getEducations().isEmpty())
            currentEducation.setText(profile.getEducations().getLast().getInstitute().getDisplayName());


        for (Education education : profile.getEducations()) {
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/education.fxml"));
            loader.setController(new EducationController(education));
            AnchorPane box = null;
            try {
                box = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            educations.getChildren().add(box);
        }

        for (String skill : profile.getSkills()) {
            AnchorPane box = new AnchorPane();
            Label label = new Label(skill);
            label.setStyle("-fx-font-size: 21px");
            box.getChildren().add(label);
            skills.getChildren().add(box);
        }

        boolean isConnection = false;
        boolean isFollowing = false;

        for (User user : Client.getProfile().getConnections()) {
            if (user.getId().equals(profile.getUser().getId())) {
                isConnection = true;
                break;
            }
        }
        for (User user : Client.getProfile().getFollowings()) {
            if (user.getId().equals(profile.getUser().getId())) {
                isFollowing = true;
                break;
            }
        }

        if (isFollowing) {
            follow.getStyleClass().add("active");
            follow.setText("Followed");
        } else {
            follow.setOnAction(event -> {
                try {
                    NetworkService.getInstance().follow(Client.getId(), profile.getUser().getId(), Client.getToken());
                    Client.getProfile().getFollowings().add(profile.getUser());
                    follow.getStyleClass().add("active");
                    follow.setText("Followed");
                } catch (ApplicationException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if (isConnection) {
            connect.getStyleClass().add("active");
            connect.setText("Connected");
        } else {
            connect.setOnAction(event -> {
                try {
                    NetworkService.getInstance().request(Client.getId(), profile.getUser().getId(), Client.getToken());
                    connect.setText("Pending...");
                } catch (ApplicationException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void connections(ActionEvent event) throws IOException {
        MainController.getInstance().setPage(Client.Page.CONNECTIONS, new ConnectionController(profile));
    }

    public void initializeEdit() {
        connect.setVisible(false);
        follow.setVisible(false);
        message.setVisible(false);

        UserService userService = UserService.getInstance();
        background.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Header Image");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PNG Files", "*.png")
            );

            File selectedFile = fileChooser.showOpenDialog(Client.getStage());
            if (selectedFile != null) {
                background.setImage(new Image(selectedFile.toURI().toString()));
                new Thread(() -> {
                    try {
                        userService.addUserHeaderImage(Client.getId(), Client.getToken(), selectedFile);
                    } catch (ApplicationException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        });

        avatar.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Profile Image");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PNG Files", "*.png")
            );

            File selectedFile = fileChooser.showOpenDialog(Client.getStage());
            if (selectedFile != null) {
                avatar.setFill(new ImagePattern(new Image(selectedFile.toURI().toString())));
                MainController.getInstance().setNavProfileImage(selectedFile);
                new Thread(() -> {
                    try {
                        userService.addUserProfileImage(Client.getId(), Client.getToken(), selectedFile);
                    } catch (ApplicationException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        });

        editProfile.setVisible(true);
        editProfile.setOnMouseClicked(event -> {
            try {
                Popup.showPopup("editProfile.fxml", new EditProfileController(this), 700, 700);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        addSkill.setVisible(true);
        addSkill.setOnMouseClicked(event -> {
            try {
                Popup.showPopup("addSkill.fxml", new SkillController(this), 700, 300);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        addEducation.setVisible(true);
        addEducation.setOnMouseClicked(event -> {
            try {
                Popup.showPopup("addEducation.fxml", new AddEducationController(this), 700, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void contactInfo(ActionEvent event) {
        try {
            Popup.showPopup("contactInfo.fxml", new ContactInfoController(profile), 700, 600);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void message(ActionEvent event) throws IOException {
        MainController.getInstance().changeTab(3);
        MainController.getInstance().setPage(Client.Page.MESSAGE, new MessageController(profile.getUser()));
    }
}
