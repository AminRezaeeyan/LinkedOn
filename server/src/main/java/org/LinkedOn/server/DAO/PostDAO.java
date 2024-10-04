package org.LinkedOn.server.DAO;

import org.LinkedOn.server.models.Post;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PostDAO {
    String addPost(Post post) throws SQLException;
    ArrayList<Post> getUserFeed(String userId) throws SQLException;
    void like(String userId, String postId) throws SQLException;
    boolean hasLiked(String userId, String postId) throws SQLException;
}
