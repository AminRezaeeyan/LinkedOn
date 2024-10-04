package org.linkedon.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.Education;
import org.linkedon.client.models.User;
import org.linkedon.client.services.EducationService;
import org.linkedon.client.services.UserService;
import org.linkedon.client.utils.Validator;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;

public class SignUpController {
    private static int step = 1;
    private static User user = new User();
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

    @FXML
    private TextField firstNameInput;

    @FXML
    private TextField lastNameInput;

    @FXML
    private TextField locationInput;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label warningLabel;

    @FXML
    private ComboBox<String> schoolInput;

    @FXML
    private ComboBox<Integer> startYearInput;

    @FXML
    private ComboBox<Integer> endYearInput;

    @FXML
    private HBox ageCondition;

    @FXML
    private CheckBox checkbox;

    @FXML
    private ToggleGroup job;

    @FXML
    private RadioButton radioButton;

    public void initialize() {
        switch (step) {
            case 1 -> {
                emailLabel.setOnMouseClicked(event -> emailInput.requestFocus());
                passwordLabel.setOnMouseClicked(event -> passwordInput.requestFocus());

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
                        setInvalidInputStyle(passwordInput, passwordWarning, "The password you provided must have at least 6 characters.");
                        isValidPass = false;
                    } else {
                        setValidInputStyle(passwordInput, passwordWarning);
                        isValidPass = true;
                    }
                });
            }
            case 3 -> welcomeLabel.setText(STR."Welcome,\{user.getFirstName()}! What's your location?");
            case 4 -> {
                for (int i = 2024; i >= 1965 ; i--) {
                    startYearInput.getItems().add(i);
                }

                for (int i = 2031; i >= 1972 ; i--) {
                    endYearInput.getItems().add(i);
                }
                for (Education.Institute institute : Education.Institute.values()){
                    schoolInput.getItems().add(institute.getDisplayName());
                }
            }
        }

    }

    public void submit(ActionEvent event) {
        if (emailInput.getText().isEmpty()) {
            emailInput.setStyle("-fx-border-color: rgba(255,0,0,0.3); -fx-border-width: 2px;");
        }

        if (passwordInput.getText().isEmpty()) {
            passwordInput.setStyle("-fx-border-color: rgba(255,0,0,0.3); -fx-border-width: 2px;");
        }

        if (!isValidPass || !isValidEmail) return;

        String email = emailInput.getText();
        String password = passwordInput.getText();

        try {
            UserService userService = UserService.getInstance();
            if (!userService.isEmailAvailable(email)) {
                emailWarning.setText("This email is already in use!");
            } else {
                user.setEmail(email);
                user.setPassword(password);
                step++;
                Client.loadPage(Client.Page.SIGN_UP_STEP2, this);
            }
        } catch (ApplicationException | IOException e) {
            e.printStackTrace();
            passwordWarning.setText("Something went wrong! Try again later.");
        }
    }

    public void handleSignIn(ActionEvent event) throws IOException {
        Client.loadPage(Client.Page.SIGN_IN, new SignInController());
    }

    private void setInvalidInputStyle(TextField inputField, Label label, String warningText) {
        inputField.setStyle("-fx-border-color: rgba(255,0,0,0.3); -fx-border-width: 2px;");
        label.setText(warningText);
    }

    private void setValidInputStyle(TextField inputField, Label label) {
        inputField.setStyle("-fx-border-color: transparent; -fx-border-width: 0;");
        label.setText("");
    }

    public void submitName(ActionEvent event) throws IOException {
        if (firstNameInput.getText().isEmpty()) {
            firstNameInput.setStyle("-fx-border-color: rgba(255,0,0,0.3); -fx-border-width: 2px;");
        }

        if (lastNameInput.getText().isEmpty()) {
            lastNameInput.setStyle("-fx-border-color: rgba(255,0,0,0.3); -fx-border-width: 2px;");
        }

        String firstName = firstNameInput.getText();
        String lastName = lastNameInput.getText();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        step++;
        Client.loadPage(Client.Page.SIGN_UP_STEP3, this);
    }

    public void submitLocation(ActionEvent event) {
        if (locationInput.getText().isEmpty()) {
            locationInput.setStyle("-fx-border-color: rgba(255,0,0,0.3); -fx-border-width: 2px;");
            return;
        }

        String location = locationInput.getText();
        user.setLocation(location);

        try {

            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            UserService userService = UserService.getInstance();

            HashMap<String, String> userCredentials = userService.addUser(user);
            user.setId(userCredentials.get("body"));
            Client.setId(userCredentials.get("body"));
            Client.setToken(userCredentials.get("authorization").substring(7));  //Remove 'Bearer' keyword

            step++;
            Client.loadPage(Client.Page.SIGN_UP_STEP4, this);
        } catch (ApplicationException | IOException e) {
            e.printStackTrace();
            warningLabel.setText("Something went wrong! Try again later");
        }
    }

    public void skip(ActionEvent event) throws IOException {
        Client.loadPage(Client.Page.SIGN_UP_STEP5, this);
    }

    public void submitEducation(ActionEvent event) {
        String school = schoolInput.getValue();
        Integer startYear = startYearInput.getValue();
        Integer endYear = endYearInput.getValue();

        if (school.isEmpty()){
            schoolInput.setStyle("-fx-border-color: rgba(255,0,0,0.3)");
            return;
        } else {
            ageCondition.setStyle("-fx-border-color: rgba(0,0,0,1)");
        }

        if (school.isEmpty()){
            schoolInput.setStyle("-fx-border-color: rgba(255,0,0,0.3)");
            return;
        } else {
            ageCondition.setStyle("-fx-border-color: rgba(0,0,0,1)");
        }

        if (startYear == null){
            startYearInput.setStyle("-fx-border-color: rgba(255,0,0,0.3)");
            return;
        } else {
            startYearInput.setStyle("-fx-border-color: rgba(0,0,0,1)");
        }

        if (endYear == null){
            endYearInput.setStyle("-fx-border-color: rgba(255,0,0,0.3)");
            return;
        } else {
            endYearInput.setStyle("-fx-border-color: rgba(0,0,0,1)");
        }

        if (!checkbox.isSelected()){
            warningLabel.setText("You should be over 16!");
            ageCondition.setStyle("-fx-border-color: rgba(255,0,0,0.3)");
            return;
        } else {
            warningLabel.setText("");
            ageCondition.setStyle("-fx-border-color: rgba(0,0,0,1)");
        }

        Education education = new Education();
        Calendar calendar = Calendar.getInstance();

        education.setUserId(Client.getId());

        for (Education.Institute institute : Education.Institute.values()){
            if (institute.getDisplayName().equals(school)){
                education.setInstitute(institute);
                break;
            }
        }

        calendar.set(Calendar.YEAR, startYear);
        education.setStartDate(new Date(calendar.getTimeInMillis()));

        calendar.set(Calendar.YEAR, endYear);
        education.setEndDate(new Date(calendar.getTimeInMillis()));

        try{
            EducationService educationService = EducationService.getInstance();
            educationService.addEducation(education, Client.getToken());
            Client.loadPage(Client.Page.SIGN_UP_STEP5, this);
        } catch (ApplicationException | IOException e){
            warningLabel.setText("Something went wrong! Try again later.");
        }
    }

    public void submitState(ActionEvent event) throws IOException {
        RadioButton selected = (RadioButton) job.getSelectedToggle();
        if (selected.equals(radioButton)){
            user.setUserState(User.UserState.OPEN_TO_WORK);

            try{
                UserService userService = UserService.getInstance();
                userService.updateUser(user, Client.getToken());
            } catch (ApplicationException | IOException e){
                e.printStackTrace();
            }
        }
        Client.loadPage(Client.Page.MAIN, MainController.getInstance());
    }
}
