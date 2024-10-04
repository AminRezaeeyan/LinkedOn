package org.linkedon.client.services;

import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.Post;
import org.linkedon.client.utils.HTTP;
import org.linkedon.client.utils.Json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class PostService {
    private static PostService instance;

    private PostService() {
    }

    public static PostService getInstance() {
        if (instance == null) instance = new PostService();
        return instance;
    }

    public String addPost(Post post, String authToken) throws ApplicationException, IOException {
        HashMap<String, String> response = HTTP.sendRequest("/posts", "POST", authToken, Json.toJson(post));
        return response.get("body");
    }

    public InputStream fetchPostMedia(String postId, String authToken) throws IOException, ApplicationException {
        return HTTP.sendMediaRequest(STR."/media/posts/\{postId}/media/png", "GET", authToken);
    }

    public void addPostMedia(String postId, String authToken, File image) throws IOException, ApplicationException {
        HTTP.sendRequest(STR."/media/posts/\{postId}/media/png", "POST", authToken, image);
    }

    public ArrayList<Post> getUserFeed(String userId, String authToken) throws IOException, ApplicationException {
        HashMap<String, String> response = HTTP.sendRequest(STR."/users/\{userId}/feed", "GET", authToken, (String) null);
        return (ArrayList<Post>) Json.fromJsonArray(response.get("body"), Post[].class);
    }

    public void addLike(String userId, String postId) throws IOException, ApplicationException {
        HTTP.sendRequest(STR."/posts/\{postId}/likes/\{userId}", "POST", Client.getToken(), (String) null);
    }
}
