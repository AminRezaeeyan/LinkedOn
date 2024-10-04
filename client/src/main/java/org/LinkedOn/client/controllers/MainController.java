package org.linkedon.client.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.*;
import org.linkedon.client.services.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainController {
    private ArrayList<VBox> boxes;

    @FXML
    private StackPane container;
    @FXML
    private VBox home;
    @FXML
    private VBox network;
    @FXML
    private VBox job;
    @FXML
    private VBox message;
    @FXML
    private VBox notification;
    @FXML
    private ImageView navProfileImage;
    @FXML
    private FontIcon searchIcon;
    @FXML
    private TextField searchInput;

    private MainController() {
    }

    private static MainController instance;

    public static MainController getInstance() {
        if (instance == null) instance = new MainController();
        return instance;
    }

    public void initialize() {
        new Thread(() -> Platform.runLater(() -> {
            try {
                setPage(Client.Page.HOME, new HomeController());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        })).start();

        Thread fetchProfileThread = new Thread(() -> Client.setProfile(fetchProfile(Client.getId())));
        Thread fetchUserFeedThread = new Thread(() -> {
            try {
                Client.setFeed(PostService.getInstance().getUserFeed(Client.getId(), Client.getToken()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ApplicationException e) {
                throw new RuntimeException(e);
            }
        });
        fetchProfileThread.start();
        fetchUserFeedThread.start();


        Rectangle clip = new Rectangle(navProfileImage.getFitWidth(), navProfileImage.getFitHeight());
        clip.setArcWidth(navProfileImage.getFitWidth());
        clip.setArcHeight(navProfileImage.getFitHeight());
        navProfileImage.setClip(clip);

        UserService userService = UserService.getInstance();
        try {
            InputStream profileImage = userService.fetchUserProfileImage(Client.getId(), Client.getToken());
            navProfileImage.setImage(new Image(profileImage));
        } catch (Exception e) {
            navProfileImage.setImage(new Image(Client.class.getResourceAsStream("assets/images/defaultProfile.png")));
        }

        boxes = new ArrayList<>();
        boxes.add(home);
        boxes.add(network);
        boxes.add(job);
        boxes.add(message);
        boxes.add(notification);
        home.getStyleClass().add("selectedBox");

        searchIcon.setOnMouseClicked(event -> {
            String searchQuery = searchInput.getText();
            if ("".equals(searchQuery)) return;

            ArrayList<User> users;
            try {
                users = userService.searchUsers(searchQuery, Client.getToken());
            } catch (ApplicationException | IOException e) {
                throw new RuntimeException(e);
            }

            VBox vBox = new VBox();
            vBox.setMaxWidth(1200);
            vBox.setSpacing(30);
            vBox.setPadding(new Insets(30, 0, 0, 0));
            for (User user : users) {
                FXMLLoader loader = new FXMLLoader(Client.class.getResource("views/userBox.fxml"));
                loader.setController(new UserBoxController(user));
                GridPane box;
                try {
                    box = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                vBox.getChildren().add(box);
            }
            container.getChildren().clear();
            container.getChildren().add(vBox);
        });

        try {
            fetchProfileThread.join();
            fetchUserFeedThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleSelectedBox(MouseEvent event) throws IOException {
        Node option = (Node) event.getTarget();
        while (!boxes.contains(option)) option = option.getParent();

        if (option == home) setPage(Client.Page.HOME, new HomeController());
        else if (option == network) setPage(Client.Page.NETWORK, new NetworkController());
        else container.getChildren().clear();

        if (option.getStyleClass().contains("selectedBox")) return;

        for (VBox box : boxes) {
            box.getStyleClass().remove("selectedBox");
        }

        option.getStyleClass().add("selectedBox");
    }

    public void changeTab(int index) {
        for (VBox box : boxes) {
            box.getStyleClass().remove("selectedBox");
        }

        switch (index) {
            case 0 -> home.getStyleClass().add("selectedBox");
            case 1 -> network.getStyleClass().add("selectedBox");
            case 2 -> job.getStyleClass().add("selectedBox");
            case 3 -> message.getStyleClass().add("selectedBox");
            case 4 -> notification.getStyleClass().add("selectedBox");
        }
    }

    public void showUserProfile(MouseEvent event) throws IOException {
        container.getChildren().clear();
        setPage(Client.Page.PROFILE, new ProfileController(Client.getProfile()));
    }

    public void showProfile(String userId) throws IOException {
        new Thread(() -> Platform.runLater(() -> {
                    try {
                        container.getChildren().clear();
                        setPage(Client.Page.PROFILE, new ProfileController(fetchProfile(userId)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        )).start();
    }

    public void setNavProfileImage(File image) {
        navProfileImage.setImage(new Image(image.toURI().toString()));
    }

    public void setPage(Client.Page page, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(Client.class.getResource(STR."views/\{page.getFxmlFileName()}"));
        loader.setController(controller);
        Node root = loader.load();

        StackPane stackPane = (StackPane) root;
        stackPane.setMinHeight(container.getHeight());
        stackPane.setMaxHeight(container.getHeight());
        container.getChildren().clear();
        container.getChildren().add(stackPane);
        container.layout();
        container.requestLayout();
    }

    public Profile fetchProfile(String userId) {
        try {
            UserService userService = UserService.getInstance();
            EducationService educationService = EducationService.getInstance();
            NetworkService networkService = NetworkService.getInstance();
            User user = userService.fetchUserFullInfo(userId, Client.getToken());
            ContactInfo contactInfo = userService.fetchContactInfo(userId, Client.getToken());
            ArrayList<User> connections = networkService.fetchUserConnections(userId, Client.getToken());
            ArrayList<User> followers = networkService.fetchFollowers(userId, Client.getToken());
            ArrayList<User> followings = networkService.fetchFollowings(userId, Client.getToken());
            ArrayList<String> skills = userService.fetchUserSkills(userId, Client.getToken());
            ArrayList<Education> educations = educationService.fetchUserEducations(userId, Client.getToken());
            return new Profile(user, contactInfo, connections, followers, followings, skills, educations);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
