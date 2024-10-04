package org.LinkedOn.server.DAO.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.LinkedOn.server.DAO.PostDAO;
import org.LinkedOn.server.models.Post;
import org.LinkedOn.server.utils.Database;
import org.LinkedOn.server.utils.Json;
import org.LinkedOn.server.utils.UUIDUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostDAOImpl implements PostDAO {
    private static PostDAOImpl instance;
    private final Connection connection;

    private PostDAOImpl() {
        this.connection = Database.connect();
    }

    public synchronized static PostDAOImpl getInstance() {
        if (instance == null) instance = new PostDAOImpl();
        return instance;
    }

    @Override
    public synchronized String addPost(Post post) throws SQLException {
        String id = UUIDUtil.generate();
        // Store the id in binary format for less memory space

        String query = "INSERT INTO posts (id, user_id, text, likes) VALUES (?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(id));
            ps.setBytes(2, UUIDUtil.UUIDToBytes(post.getUserId()));
            ps.setString(3, post.getText());
            ps.setInt(4, post.getLikes());
            ps.executeUpdate();
        }
        return id;
    }

    @Override
    public ArrayList<Post> getUserFeed(String userId) throws SQLException {
        ArrayList<Post> feed = new ArrayList<>();
        String query = "CALL getUserFeed(?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(userId));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    feed.add(new Post(
                            UUIDUtil.bytesToUUID(rs.getBytes("id")),
                            UUIDUtil.bytesToUUID(rs.getBytes("user_id")),
                            rs.getString("text"),
                            rs.getInt("likes"),
                            rs.getBoolean("hasLiked")
                    ));
                }
            }
        }
        return feed;
    }

    @Override
    public void like(String userId, String postId) throws SQLException {
        String query = "INSERT INTO likes (user_id, post_id) VALUES (?,?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(userId));
            ps.setBytes(2, UUIDUtil.UUIDToBytes(postId));
            ps.executeUpdate();
        }
    }

    @Override
    public boolean hasLiked(String userId, String postId) throws SQLException {
        String query = "SELECT * FROM likes WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(userId));
            ps.setBytes(2, UUIDUtil.UUIDToBytes(postId));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
