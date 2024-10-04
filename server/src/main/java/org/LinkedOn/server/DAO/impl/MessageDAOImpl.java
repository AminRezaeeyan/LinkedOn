package org.LinkedOn.server.DAO.impl;

import org.LinkedOn.server.DAO.MessageDAO;
import org.LinkedOn.server.models.Message;
import org.LinkedOn.server.utils.Database;
import org.LinkedOn.server.utils.UUIDUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MessageDAOImpl implements MessageDAO {
    private static MessageDAOImpl instance;
    private final Connection connection;

    private MessageDAOImpl() {
        connection = Database.connect();
    }

    public synchronized static MessageDAOImpl getInstance() {
        if (instance == null) instance = new MessageDAOImpl();
        return instance;
    }

    @Override
    public void addMessage(Message message) throws SQLException {
        String query = "INSERT INTO messages (sender_id, receiver_id, text) VALUES (?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(message.getSenderId()));
            ps.setBytes(2, UUIDUtil.UUIDToBytes(message.getReceiverId()));
            ps.setString(3, message.getText());
            ps.executeUpdate();
        }
    }

    @Override
    public ArrayList<Message> getMessages(String userId1, String userId2) throws SQLException {
        ArrayList<Message> chat = new ArrayList<>();
        String query = "SELECT * FROM messages WHERE" +
                " (sender_id = ? AND receiver_id = ?) OR (receiver_id = ? AND sender_id = ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(userId1));
            ps.setBytes(2, UUIDUtil.UUIDToBytes(userId2));
            ps.setBytes(3, UUIDUtil.UUIDToBytes(userId1));
            ps.setBytes(4, UUIDUtil.UUIDToBytes(userId2));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    chat.add(new Message(
                            UUIDUtil.bytesToUUID(rs.getBytes("sender_id")),
                            UUIDUtil.bytesToUUID(rs.getBytes("receiver_id")),
                            rs.getString("text")
                    ));
                }
            }
        }
        return chat;
    }
}
