package org.LinkedOn.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.LinkedOn.server.DAO.FollowDAO;
import org.LinkedOn.server.DAO.impl.FollowDAOImpl;
import org.LinkedOn.server.models.User;
import org.LinkedOn.server.utils.Json;

import java.sql.SQLException;
import java.util.ArrayList;

public class FollowController {
    private static FollowController instance;
    private final FollowDAO followDAO;

    private FollowController(){
        followDAO = FollowDAOImpl.getInstance();
    }

    public synchronized static FollowController getInstance(){
        if (instance == null) instance = new FollowController();
        return instance;
    }

    public synchronized void addFollow(String followerId, String followedId) throws SQLException {
        followDAO.addFollow(followerId, followedId);
    }

    public synchronized String getFollowers(String userId) throws SQLException, JsonProcessingException {
        ArrayList<User> users = followDAO.getFollowers(userId);
        return Json.toJsonArrayWithView(users, Json.Views.Brief.class);
    }

    public synchronized String getFollowings(String userId) throws SQLException, JsonProcessingException {
        ArrayList<User> users = followDAO.getFollowings(userId);
        return Json.toJsonArrayWithView(users, Json.Views.Brief.class);
    }

    public synchronized void removeFollow(String followerId, String followedId) throws SQLException {
        followDAO.removeFollow(followerId, followedId);
    }
}
