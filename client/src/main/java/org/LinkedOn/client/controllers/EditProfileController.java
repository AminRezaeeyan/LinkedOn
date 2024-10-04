package org.linkedon.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.kordamp.ikonli.javafx.FontIcon;
import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.Profile;
import org.linkedon.client.services.UserService;
import org.linkedon.client.utils.Popup;

import java.io.IOException;

public class EditProfileController {
    @FXML
    private AnchorPane popUp;
    @FXML
    private FontIcon exitPopUp;

    @FXML
    private TextField firstNameInput;
    @FXML
    private TextField lastNameInput;
    @FXML
    private TextField additionalNameInput;
    @FXML
    private TextField headlineInput;
    @FXML
    private TextField locationInput;
    @FXML
    private TextField industryInput;
    Profile profile;
    ProfileController profileController;

    public EditProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }

    public void initialize() {
        profile = Client.getProfile();
        exitPopUp.setOnMouseClicked(event -> Popup.hidePopup());

        String firstName = profile.getUser().getFirstName();
        String lastName = profile.getUser().getLastName();
        String additionalName = profile.getUser().getAdditionalName();
        String headline = profile.getUser().getHeadline();
        String location = profile.getUser().getLocation();
        String industry = profile.getUser().getIndustry();

        firstNameInput.setPromptText(profile.getUser().getFirstName());
        lastNameInput.setPromptText(profile.getUser().getLastName());
        if (additionalName != null) additionalNameInput.setPromptText(additionalName);
        if (headline != null) headlineInput.setPromptText(headline);
        if (industry != null) industryInput.setPromptText(industry);
        if (location != null) locationInput.setPromptText(location);
    }

    public void submit(ActionEvent event) {
        String firstName = firstNameInput.getText();
        String lastName = lastNameInput.getText();
        String additionalName = additionalNameInput.getText();
        String headline = headlineInput.getText();
        String location = locationInput.getText();
        String industry = industryInput.getText();

        if (!firstName.equals("")) profile.getUser().setFirstName(firstName);
        if (!lastName.equals("")) profile.getUser().setLastName(lastName);
        if (!additionalName.equals("")) profile.getUser().setAdditionalName(additionalName);
        if (!location.equals("")) profile.getUser().setLocation(location);
        if (!industry.equals("")) profile.getUser().setIndustry(industry);
        if (!headline.equals("")) profile.getUser().setHeadline(headline);

        try {
            UserService.getInstance().updateUser(profile.getUser(), Client.getToken());
        } catch (ApplicationException | IOException e) {
            throw new RuntimeException(e);
        }

        profileController.initialize();
        Popup.hidePopup();
    }
}
