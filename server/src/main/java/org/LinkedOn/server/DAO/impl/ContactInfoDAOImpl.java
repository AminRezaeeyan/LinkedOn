package org.LinkedOn.server.DAO.impl;

import org.LinkedOn.server.DAO.ContactInfoDAO;
import org.LinkedOn.server.models.ContactInfo;
import org.LinkedOn.server.utils.Database;
import org.LinkedOn.server.utils.UUIDUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactInfoDAOImpl implements ContactInfoDAO {
    private static ContactInfoDAOImpl instance;
    private final Connection connection;

    private ContactInfoDAOImpl() {
        connection = Database.connect();
    }

    public static ContactInfoDAOImpl getInstance() {
        if (instance == null) instance = new ContactInfoDAOImpl();
        return instance;
    }

    @Override
    public ContactInfo fetchContactInfo(String userId) throws SQLException {
        String query = "SELECT * FROM contact_infos WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(userId));
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new ContactInfo(
                        userId,
                        rs.getString("address"),
                        rs.getString("website"),
                        rs.getString("phone_number"),
                        ContactInfo.PhoneType.valueOf(rs.getString("phone_type")),
                        rs.getDate("birthday"),
                        rs.getString("instant_message"),
                        rs.getString("instant_message_type")
                );
            }
        }
    }

    @Override
    public void updateContactInfo(ContactInfo contactInfo) throws SQLException {
        String query = "UPDATE contact_infos SET address = ?, website = ?, phone_number = ?, phone_type = ?, birthday = ?, instant_message = ?, instant_message_type = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, contactInfo.getAddress());
            ps.setString(2, contactInfo.getWebsite());
            ps.setString(3, contactInfo.getPhoneNumber());
            ps.setString(4, contactInfo.getPhoneType().name());
            ps.setDate(5, contactInfo.getBirthday());
            ps.setString(6, contactInfo.getInstantMessaging());
            ps.setString(7, contactInfo.getInstantMessagingType());
            ps.setBytes(8, UUIDUtil.UUIDToBytes(contactInfo.getUserId()));
            ps.executeUpdate();
        }
    }
}
