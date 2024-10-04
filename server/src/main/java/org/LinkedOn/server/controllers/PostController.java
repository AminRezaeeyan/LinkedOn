package org.LinkedOn.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.LinkedOn.server.DAO.PostDAO;
import org.LinkedOn.server.DAO.impl.PostDAOImpl;
import org.LinkedOn.server.models.Post;
import org.LinkedOn.server.utils.Json;

import java.sql.SQLException;
import java.util.ArrayList;

public class PostController {
    private static PostController instance;
    private final PostDAO postDAO;

    private PostController() {
        this.postDAO = PostDAOImpl.getInstance();
    }

    public static PostController getInstance() {
        if (instance == null) instance = new PostController();
        return instance;
    }

    public synchronized String addPost(Post post) throws SQLException {
        String postId = postDAO.addPost(post);
        return postId;
    }

    public synchronized String getUserFeed(String userId) throws SQLException, JsonProcessingException {
        ArrayList<Post> feed = postDAO.getUserFeed(userId);
        return Json.toJsonArray(feed);
    }

    public synchronized void addLike(String userId, String postId) throws SQLException {
        if (postDAO.hasLiked(userId, postId)) return;
        postDAO.like(userId, postId);
    }
}
