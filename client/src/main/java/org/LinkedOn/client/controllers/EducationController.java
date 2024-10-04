package org.linkedon.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.linkedon.client.models.Education;

public class EducationController {
    private Education education;

    @FXML
    private Label school;
    @FXML
    private Label degreeField;
    @FXML
    private Label date;

    public EducationController(Education education) {
        this.education = education;
    }

    public void initialize() {
        String degree = education.getDegree().getDisplayName();
        String field = education.getField();
        String startDate = Integer.toString(education.getStartDate().toLocalDate().getYear());
        String endDate = Integer.toString(education.getEndDate().toLocalDate().getYear());

        school.setText(education.getInstitute().getDisplayName());
        if (field != null && degree != null) degreeField.setText(STR."\{degree}, \{field}");
        if (startDate != null && endDate != null) date.setText(STR."\{startDate}, \{endDate}");
        else if (startDate != null) date.setText(startDate);
    }
}
