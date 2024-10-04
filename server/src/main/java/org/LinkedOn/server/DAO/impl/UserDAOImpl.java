package org.LinkedOn.server.DAO.impl;

import org.LinkedOn.server.DAO.UserDAO;
import org.LinkedOn.server.models.User;
import org.LinkedOn.server.utils.Database;
import org.LinkedOn.server.utils.Hash;
import org.LinkedOn.server.utils.UUIDUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private static UserDAOImpl instance;
    private final Connection connection;

    private UserDAOImpl() {
        connection = Database.connect();
    }

    public static synchronized UserDAOImpl getInstance() {
        if (instance == null) instance = new UserDAOImpl();
        return instance;
    }

    @Override
    public synchronized String addUser(User user) throws SQLException {
        String id = UUIDUtil.generate();
        byte[] binaryId = UUIDUtil.UUIDToBytes(id);  // Store the id in binary format for less memory space
        String hashedPassword = Hash.hash(user.getPassword());  // Hash the password for more security

        String query = "INSERT INTO users (id, email, password, first_name, last_name, bio, additional_name, headline, location, user_state, industry, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, binaryId);
            ps.setString(2, user.getEmail());
            ps.setString(3, hashedPassword);
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setString(6, user.getBio());
            ps.setString(7, user.getAdditionalName());
            ps.setString(8, user.getHeadline());
            ps.setString(9, user.getLocation());
            ps.setString(10, user.getUserState().name());
            ps.setString(11, user.getIndustry());
            ps.setTimestamp(12, user.getCreatedAt());
            ps.executeUpdate();
        }
        return id;
    }

    @Override
    public synchronized User fetchUserById(String id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(id));
            try (ResultSet rs = ps.executeQuery()) {
                List<User> users = Database.mapResultSetToList(rs, this::mapRowToUser);
                if (users.isEmpty()) return null;
                return users.get(0);
            }
        }
    }

    @Override
    public synchronized User fetchUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                List<User> users = Database.mapResultSetToList(rs, this::mapRowToUser);
                if (users.isEmpty()) return null;
                return users.get(0);
            }
        }
    }

    @Override
    public synchronized ArrayList<User> fetchUsers() throws SQLException {
        String query = "SELECT * FROM users";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                return (ArrayList<User>) Database.mapResultSetToList(rs, this::mapRowToUser);
            }
        }
    }

    @Override
    public synchronized void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET email = ?, first_name = ?, last_name = ?, bio = ?, additional_name = ?, headline = ?, location = ?, user_state = ?, industry = ?, created_at = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getBio());
            ps.setString(5, user.getAdditionalName());
            ps.setString(6, user.getHeadline());
            ps.setString(7, user.getLocation());
            ps.setString(8, user.getUserState().name());
            ps.setString(9, user.getIndustry());
            ps.setTimestamp(10, user.getCreatedAt());
            ps.setBytes(11, UUIDUtil.UUIDToBytes(user.getId()));
            ps.executeUpdate();
        }
    }

    @Override
    public synchronized void removeUser(String id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(id));
            ps.executeUpdate();
        }
    }

    @Override
    public synchronized boolean userExists(String id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(id));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public synchronized boolean emailExists(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public synchronized ArrayList<User> searchUsers(String searchQuery, int limit) throws SQLException {
        String query = "SELECT * FROM users WHERE first_name LIKE ? OR last_name LIKE ? OR headline LIKE ? OR industry LIKE ? LIMIT ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            String searchPattern = "%" + searchQuery + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            ps.setInt(5, limit);
            try (ResultSet rs = ps.executeQuery()) {
                return (ArrayList<User>) Database.mapResultSetToList(rs, this::mapRowToUser);
            }
        }
    }


    protected synchronized User mapRowToUser(ResultSet rs) {
        try {
            return new User(
                    UUIDUtil.bytesToUUID(rs.getBytes("id")),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("headline"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("additional_name"),
                    rs.getString("bio"),
                    rs.getString("location"),
                    rs.getString("industry"),
                    User.UserState.valueOf(rs.getString("user_state")),
                    rs.getTimestamp("created_at")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Failed to map resultSet to User object", e);
        }
    }
}