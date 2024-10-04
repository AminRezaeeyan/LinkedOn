package org.linkedon.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.linkedon.client.controllers.LandingPageController;
import org.linkedon.client.controllers.MainController;
import org.linkedon.client.models.Post;
import org.linkedon.client.models.Profile;

import java.io.IOException;
import java.util.ArrayList;


public class Client extends Application {
    private static String id;
    private static String token;
    private static Profile profile;
    private static ArrayList<Post> feed;
    private static Stage stage;
    private static Scene mainScene;
    private static Theme theme = Theme.LIGHT;

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Client.id = id;
    }

    public static String getToken() {
        return token;
    }
    public static void setToken(String token) {
        Client.token = token;
    }

    public static Profile getProfile() {
        return profile;
    }

    public static void setProfile(Profile profile) {
        Client.profile = profile;
    }

    public static ArrayList<Post> getFeed() {
        return feed;
    }

    public static void setFeed(ArrayList<Post> feed) {
        Client.feed = feed;
    }

    public static Stage getStage() {
        return stage;
    }

    public static Theme getTheme() {
        return theme;
    }

    public static Scene getMainScene(){
        return mainScene;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setTitle("LinkedOn");
        stage.getIcons().add(new Image(Client.class.getResourceAsStream("assets/images/linkedinIcon.png")));

        loadPage(Page.LANDING_PAGE, new LandingPageController());
        stage.setScene(mainScene);
        stage.show();
    }

    public static void loadPage(Page page, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(Client.class.getResource(STR."views/\{page.getFxmlFileName()}"));
        loader.setController(controller);
        Parent newRoot = loader.load();

        if (theme == Theme.DARK) {
            newRoot.getStyleClass().add("dark");
        }

        if (mainScene == null) {
            mainScene = new Scene(newRoot);
        } else {
            mainScene.setRoot(newRoot);
        }
    }


    public static void toggleTheme(){
        if (mainScene.getRoot().getStyleClass().contains("dark")) {
            theme = Theme.LIGHT;
            mainScene.getRoot().getStyleClass().remove("dark");
        } else {
            mainScene.getRoot().getStyleClass().add("dark");
            theme = Theme.DARK;
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public enum Page {
        LANDING_PAGE("landingPage.fxml"),
        SIGN_IN("signInPage.fxml"),
        SIGN_UP_STEP1("signUpStep1.fxml"),
        SIGN_UP_STEP2("signUpStep2.fxml"),
        SIGN_UP_STEP3("signUpStep3.fxml"),
        SIGN_UP_STEP4("signUpStep4.fxml"),
        SIGN_UP_STEP5("signUpStep5.fxml"),
        MAIN("main.fxml"),
        NETWORK("network.fxml"),
        HOME("home.fxml"),
        FOLLOWERS("followers.fxml"),
        CONNECTIONS("connections.fxml"),
        MESSAGE("message.fxml"),
        PROFILE("profile.fxml");
        private final String fxmlFileName;

        Page(String fxmlFileName) {
            this.fxmlFileName = fxmlFileName;
        }

        public String getFxmlFileName() {
            return fxmlFileName;
        }
    }

    public enum Theme {
        LIGHT,
        DARK
    }
}