package org.linkedon.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.javafx.FontIcon;
import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.ContactInfo;
import org.linkedon.client.models.Profile;
import org.linkedon.client.services.UserService;
import org.linkedon.client.utils.Popup;

import java.io.IOException;
import java.sql.Date;

public class ContactInfoController {
    private boolean editable;
    Profile profile;

    @FXML
    private FontIcon exitPopUp;
    @FXML
    private TextField email;
    @FXML
    private TextField address;
    @FXML
    private TextField phone;
    @FXML
    private ComboBox phoneType;
    @FXML
    private DatePicker birthday;
    @FXML
    private Button submitBtn;

    public ContactInfoController(Profile profile) {
        this.profile = profile;
        this.editable = profile.getUser().getId().equals(Client.getId());
    }

    public void initialize() {
        exitPopUp.setOnMouseClicked(event -> Popup.hidePopup());

        for (ContactInfo.PhoneType type : ContactInfo.PhoneType.values()) {
            phoneType.getItems().add(type.name());
        }

        String emailValue = profile.getUser().getEmail();
        String phoneValue = profile.getContactInfo().getPhoneNumber();
        ContactInfo.PhoneType phoneTypeValue = profile.getContactInfo().getPhoneType();
        String addressValue = profile.getContactInfo().getAddress();
        Date birthdayValue = profile.getContactInfo().getBirthday();

        email.setText(profile.getUser().getEmail());
        if (phoneValue != null) phone.setText(phoneValue);
        if (phoneTypeValue != null) phoneType.setValue(phoneTypeValue.name());
        if (addressValue != null) address.setText(addressValue);
        if (birthdayValue != null) birthday.setValue(birthdayValue.toLocalDate());

        if (!editable) {
            phone.setEditable(false);
            phoneType.setDisable(true);
            address.setEditable(false);
            birthday.setDisable(true);
            submitBtn.setVisible(false);
        }
    }

    public void submit() {
        String phoneValue = phone.getText();
        ContactInfo.PhoneType phoneTypeValue = ContactInfo.PhoneType.valueOf((String) phoneType.getValue());
        String addressValue = address.getText();
        Date birthdayValue = Date.valueOf(birthday.getValue());

        profile.getContactInfo().setAddress(addressValue);
        profile.getContactInfo().setPhoneNumber(phoneValue);
        profile.getContactInfo().setPhoneType(phoneTypeValue);
        profile.getContactInfo().setBirthday(birthdayValue);

        try {
            UserService.getInstance().updateContactInfo(profile.getContactInfo(), Client.getToken());
        } catch (ApplicationException | IOException e) {
            throw new RuntimeException(e);
        }

        Popup.hidePopup();
    }
}
