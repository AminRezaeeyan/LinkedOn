package org.linkedon.client.controllers;

import org.linkedon.client.Client;
import org.linkedon.client.exceptions.*;
import org.linkedon.client.services.UserService;
import org.linkedon.client.utils.Validator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.HashMap;

public class SignInController {
    private static boolean isValidEmail = false;
    private static boolean isValidPass = false;


    @FXML
    private TextField emailInput;

    @FXML
    private Label emailLabel;

    @FXML
    private Label emailWarning;

    @FXML
    private TextField passwordInput;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label passwordWarning;

    public void initialize() {
        emailLabel.setLabelFor(emailInput);
        passwordLabel.setLabelFor(passwordInput);
        emailLabel.setOnMouseClicked(event -> emailInput.requestFocus());
        passwordLabel.setOnMouseClicked(event -> passwordInput.requestFocus());

        emailInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                emailLabel.setTranslateY(-15);
                emailLabel.setTranslateX(-5);
                emailLabel.setStyle("-fx-font-size: 14px");
            } else {

                if (emailInput.getText().isEmpty()) {
                    emailLabel.setTranslateY(0);
                    emailLabel.setTranslateX(0);
                    emailLabel.setStyle("-fx-font-size: 20px");
                }
            }
        });

        passwordInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                passwordLabel.setTranslateY(-15);
                passwordLabel.setStyle("-fx-font-size: 14px");
                passwordLabel.setTranslateX(-5);
            } else {

                if (passwordInput.getText().isEmpty()) {
                    passwordLabel.setTranslateY(0);
                    passwordLabel.setTranslateX(0);
                    passwordLabel.setStyle("-fx-font-size: 20px");
                }
            }
        });

        emailInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!Validator.isValidEmail(newValue)) {
                setInvalidInputStyle(emailInput, emailWarning, "Please enter a valid email");
                isValidEmail = false;
            } else {
                setValidInputStyle(emailInput, emailWarning);
                isValidEmail = true;
            }
        });

        passwordInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!Validator.isValidPassword(newValue)) {
                setInvalidInputStyle(passwordInput, passwordWarning, "The password you provided must have at least 6 characters");
                isValidPass = false;
            } else {
                setValidInputStyle(passwordInput, passwordWarning);
                isValidPass = true;
            }
        });
    }

    public void submit(ActionEvent event) throws IOException, ApplicationException {
        if (emailInput.getText().isEmpty()) {
            emailInput.setStyle("-fx-border-color: rgba(255,0,0,0.3); -fx-border-width: 2px;");
        }

        if (passwordInput.getText().isEmpty()) {
            passwordInput.setStyle("-fx-border-color: rgba(255,0,0,0.3); -fx-border-width: 2px;");
        }

        if (!isValidPass || !isValidEmail) return;

        try {
            UserService userService = UserService.getInstance();
            HashMap<String, String> response = userService.authenticateUser(emailInput.getText(), passwordInput.getText());
            Client.setId(response.get("body"));
            Client.setToken(response.get("authorization").substring(7)); //Remove 'Bearer' keyword
            Client.loadPage(Client.Page.MAIN, MainController.getInstance());
        } catch (UnauthorizedException e) {
            passwordWarning.setText("Wrong email or password. Try again or create an account");
        } catch (ApplicationException | IOException e) {
            e.printStackTrace();
            passwordWarning.setText("Something went wrong! Try again later");
        }
    }

    public void handleSignUp(ActionEvent event) throws IOException {
        Client.loadPage(Client.Page.SIGN_UP_STEP1, new SignUpController());
    }

    private void setInvalidInputStyle(TextField inputField, Label label, String warningText) {
        inputField.setStyle("-fx-border-color: rgba(255,0,0,0.3); -fx-border-width: 2px;");
        label.setText(warningText);
    }

    private void setValidInputStyle(TextField inputField, Label label) {
        inputField.setStyle("-fx-border-color: transparent; -fx-border-width: 0;");
        label.setText("");
    }
}
