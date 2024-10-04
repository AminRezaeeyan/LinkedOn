package org.linkedon.client.models;

public class Post {
    private String id, userId, text;
    private int likes;
    private boolean hasLiked;

    public Post(String id, String userId, String text, int likes, boolean hasLiked) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.likes = likes;
        this.hasLiked = hasLiked;
    }

    public Post() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean HasLiked() {
        return hasLiked;
    }

    public void setHasLiked(boolean hasLiked) {
        this.hasLiked = hasLiked;
    }
}
