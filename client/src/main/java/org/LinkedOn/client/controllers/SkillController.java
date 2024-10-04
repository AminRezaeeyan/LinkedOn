package org.linkedon.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.services.UserService;
import org.linkedon.client.utils.Popup;

import java.io.IOException;
import java.util.ArrayList;

public class SkillController {
    @FXML
    private ComboBox skillInput;

    @FXML
    private FontIcon exitPopUp;

    private ProfileController profileController;

    public SkillController(ProfileController profileController) {
        this.profileController = profileController;
    }

    public void initialize() {
        exitPopUp.setOnMouseClicked(event -> Popup.hidePopup());
        ArrayList<String> skills;
        try {
            skills = UserService.getInstance().fetchSkills(Client.getToken());
        } catch (IOException | ApplicationException e) {
            throw new RuntimeException(e);
        }

        for (String skill : skills) {
            skillInput.getItems().add(skill);
        }
    }

    public void submit() {
        String skill = skillInput.getValue().toString();
        if (skill != null && !"".equals(skill)) {
            try {
                UserService.getInstance().addSkill(skill, Client.getId(), Client.getToken());
                Client.getProfile().getSkills().add(skill);
            } catch (IOException | ApplicationException e) {
                throw new RuntimeException(e);
            }
        }
        profileController.initialize();
        Popup.hidePopup();
    }
}
