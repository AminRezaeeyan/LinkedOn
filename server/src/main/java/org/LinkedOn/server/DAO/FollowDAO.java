package org.LinkedOn.server.DAO;

import org.LinkedOn.server.models.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface FollowDAO {
    void addFollow(String followerId, String followedId) throws SQLException;

    ArrayList<User> getFollowers(String userId) throws SQLException;

    ArrayList<User> getFollowings(String userId) throws SQLException;

    void removeFollow(String followerId, String followedId) throws SQLException;
}
