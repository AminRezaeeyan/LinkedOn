package org.LinkedOn.server.DAO.impl;

import org.LinkedOn.server.DAO.FollowDAO;
import org.LinkedOn.server.models.User;
import org.LinkedOn.server.utils.Database;
import org.LinkedOn.server.utils.UUIDUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowDAOImpl implements FollowDAO {
    private static FollowDAOImpl instance;
    private final Connection connection;

    private FollowDAOImpl() {
        connection = Database.connect();
    }

    public static synchronized FollowDAOImpl getInstance() {
        if (instance == null) instance = new FollowDAOImpl();
        return instance;
    }

    @Override
    public synchronized void addFollow(String followerId, String followedId) throws SQLException {
        String query = "INSERT INTO follows VALUES(?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(followerId));
            ps.setBytes(2, UUIDUtil.UUIDToBytes(followedId));
            ps.executeUpdate();
        }
    }

    @Override
    public synchronized ArrayList<User> getFollowers(String userId) throws SQLException {
        String query = "SELECT u.* FROM follows " +
                "JOIN users u ON u.id = follower_id WHERE followed_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(userId));
            try (ResultSet rs = ps.executeQuery()) {
                List<User> users = Database.mapResultSetToList(rs, UserDAOImpl.getInstance()::mapRowToUser);
                return (ArrayList<User>) users;
            }
        }
    }

    @Override
    public synchronized ArrayList<User> getFollowings(String userId) throws SQLException {
        String query = "SELECT u.* FROM follows " +
                "JOIN users u ON u.id = followed_id WHERE follower_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(userId));
            try (ResultSet rs = ps.executeQuery()) {
                List<User> users = Database.mapResultSetToList(rs, UserDAOImpl.getInstance()::mapRowToUser);
                return (ArrayList<User>) users;
            }
        }
    }

    @Override
    public synchronized void removeFollow(String followerId, String followedId) throws SQLException {
        String query = "DELETE FROM follows WHERE follower_id = ? AND followed_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(followerId));
            ps.setBytes(2, UUIDUtil.UUIDToBytes(followedId));
            ps.executeUpdate();
        }
    }
}
