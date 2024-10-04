package org.LinkedOn.server.DAO;

import org.LinkedOn.server.models.ContactInfo;

import java.sql.SQLException;

public interface ContactInfoDAO {
    ContactInfo fetchContactInfo(String userId) throws SQLException;

    void updateContactInfo(ContactInfo contactInfo) throws SQLException;

    // A new contact info is created when a new user signs up
    // And the contact info is removed when the user deletes account
    // This happens using database triggers automatically
}
