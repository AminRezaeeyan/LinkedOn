package org.linkedon.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.javafx.FontIcon;
import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.Education;
import org.linkedon.client.services.EducationService;
import org.linkedon.client.utils.Popup;

import java.io.IOException;
import java.sql.Date;

public class AddEducationController {
    private ProfileController profileController;

    @FXML
    private FontIcon exitPopUp;
    @FXML
    private ComboBox schoolInput;
    @FXML
    private ComboBox degreeInput;
    @FXML
    private TextField fieldInput;
    @FXML
    private DatePicker startDateInput;
    @FXML
    private DatePicker endDateInput;

    public AddEducationController(ProfileController profileController) {
        this.profileController = profileController;
    }

    public void initialize() {
        exitPopUp.setOnMouseClicked(event -> Popup.hidePopup());

        for (Education.Institute institute : Education.Institute.values()) {
            schoolInput.getItems().add(institute.getDisplayName());
        }

        for (Education.Degree degree : Education.Degree.values()) {
            degreeInput.getItems().add(degree.getDisplayName());
        }
    }

    public void submit() {
        Education.Institute school = null;
        Education.Degree degree = null;

        for (Education.Institute institute : Education.Institute.values()) {
            if (institute.getDisplayName().equals((String) schoolInput.getValue())) {
                school = institute;
                break;
            }
        }

        for (Education.Degree degreeEnum : Education.Degree.values()) {
            if (degreeEnum.getDisplayName().equals((String) degreeInput.getValue())) {
                degree = degreeEnum;
                break;
            }
        }

        String field = fieldInput.getText();
        Date startDate = Date.valueOf(startDateInput.getValue());
        Date endDate = Date.valueOf(endDateInput.getValue());

        if (school == null || degree == null || "".equals(field) || startDate == null || endDate == null) return;

        Education education = new Education();
        education.setUserId(Client.getId());
        education.setInstitute(school);
        education.setDegree(degree);
        education.setField(field);
        education.setStartDate(startDate);
        education.setEndDate(endDate);

        try {
            String id = EducationService.getInstance().addEducation(education, Client.getToken());
            education.setId(id);
        } catch (ApplicationException | IOException e) {
            throw new RuntimeException(e);
        }

        Client.getProfile().getEducations().add(education);
        profileController.initialize();
        Popup.hidePopup();
    }
}
