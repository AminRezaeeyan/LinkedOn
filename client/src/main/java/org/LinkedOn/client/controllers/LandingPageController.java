package org.linkedon.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import org.linkedon.client.Client;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;


public class LandingPageController {

    @FXML
    private FontIcon themeIcon;

    public void handleLogin(ActionEvent event) throws IOException {
        Client.loadPage(Client.Page.SIGN_IN, new SignInController());
    }

    public void handleSignUp(ActionEvent event) throws IOException {
        Client.loadPage(Client.Page.SIGN_UP_STEP1, new SignUpController());
    }

    public void handleTheme(ActionEvent even) {
        Client.toggleTheme();

        switch (Client.getTheme()) {
            case LIGHT -> {
                themeIcon.setFill(Color.BLACK);
                themeIcon.setIconLiteral("fa-sun-o");
            }
            case DARK -> {
                themeIcon.setFill(Color.WHITE);
                themeIcon.setIconLiteral("fa-moon-o");
            }
        }
    }
}
