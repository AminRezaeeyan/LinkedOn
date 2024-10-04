package org.LinkedOn.server.DAO.impl;

import org.LinkedOn.server.DAO.ConnectDAO;
import org.LinkedOn.server.models.Connect;
import org.LinkedOn.server.models.User;
import org.LinkedOn.server.utils.Database;
import org.LinkedOn.server.utils.UUIDUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectDAOImpl implements ConnectDAO {
    private static ConnectDAOImpl instance;
    private final Connection connection;

    private ConnectDAOImpl() {
        connection = Database.connect();
    }

    public static synchronized ConnectDAOImpl getInstance() {
        if (instance == null) {
            instance = new ConnectDAOImpl();
        }
        return instance;
    }

    @Override
    public synchronized void addConnection(Connect connect) throws SQLException {
        String query = "INSERT INTO connects VALUES(?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(connect.getUserId1()));
            ps.setBytes(2, UUIDUtil.UUIDToBytes(connect.getUserId2()));
            ps.setString(3, connect.getStatus().toString().toUpperCase());
            ps.executeUpdate();
        }
    }

    @Override
    public synchronized void updateConnection(Connect connect) throws SQLException {
        String query = "UPDATE connects SET status = ?" +
                "WHERE (user_id_1 = ? AND user_id_2 = ?)" +
                " OR (user_id_2 = ? AND user_id_1 = ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            byte[] binaryId1 = UUIDUtil.UUIDToBytes(connect.getUserId1());
            byte[] binaryId2 = UUIDUtil.UUIDToBytes(connect.getUserId2());
            ps.setString(1, connect.getStatus().toString().toUpperCase());
            ps.setBytes(2, binaryId1);
            ps.setBytes(3, binaryId2);
            ps.setBytes(4, binaryId1);
            ps.setBytes(5, binaryId2);
            ps.executeUpdate();
        }
    }

    @Override
    public synchronized ArrayList<User> getConnections(String userId) throws SQLException {
        String query = "CALL getConnections(?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(userId));
            try (ResultSet rs = ps.executeQuery()) {
                List<User> users = Database.mapResultSetToList(rs, UserDAOImpl.getInstance()::mapRowToUser);
                return (ArrayList<User>) users;
            }
        }
    }

    @Override
    public synchronized ArrayList<User> getConnectionRequests(String userId) throws SQLException {
        String query = "CALL getConnectionRequests(?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(userId));
            try (ResultSet rs = ps.executeQuery()) {
                List<User> users = Database.mapResultSetToList(rs, UserDAOImpl.getInstance()::mapRowToUser);
                return (ArrayList<User>) users;
            }
        }
    }

    @Override
    public synchronized void removeConnection(String userId1, String userId2) throws SQLException {
        String query = "DELETE FROM connects " +
                "WHERE (user_id_1 = ? AND user_id_2 = ?)" +
                " OR (user_id_2 = ? AND user_id_1 = ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            byte[] binaryId1 = UUIDUtil.UUIDToBytes(userId1);
            byte[] binaryId2 = UUIDUtil.UUIDToBytes(userId2);
            ps.setBytes(1, binaryId1);
            ps.setBytes(2, binaryId2);
            ps.setBytes(3, binaryId1);
            ps.setBytes(4, binaryId2);
            ps.executeUpdate();
        }
    }

    @Override
    public synchronized boolean connectionExists(String userId1, String userId2) throws SQLException {
        String query = "SELECT * FROM connects " +
                "WHERE (user_id_1 = ? AND user_id_2 = ?)" +
                " OR (user_id_2 = ? AND user_id_1 = ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            byte[] binaryId1 = UUIDUtil.UUIDToBytes(userId1);
            byte[] binaryId2 = UUIDUtil.UUIDToBytes(userId2);
            ps.setBytes(1, binaryId1);
            ps.setBytes(2, binaryId2);
            ps.setBytes(3, binaryId1);
            ps.setBytes(4, binaryId2);
            try(ResultSet rs = ps.executeQuery()){
                return rs.next();
            }
        }
    }
}
