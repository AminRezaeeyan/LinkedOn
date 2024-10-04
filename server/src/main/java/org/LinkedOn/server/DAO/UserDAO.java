package org.LinkedOn.server.DAO;

import org.LinkedOn.server.models.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserDAO {
    String addUser(User user) throws SQLException;  // Returns the id of user (UUID)

    User fetchUserById(String id) throws SQLException;

    User fetchUserByEmail(String email) throws SQLException;

    ArrayList<User> fetchUsers() throws SQLException;

    ArrayList<User> searchUsers(String searchQuery, int limit) throws SQLException;

    void updateUser(User user) throws SQLException;

    void removeUser(String id) throws SQLException;

    boolean userExists(String id) throws SQLException;

    boolean emailExists(String email) throws SQLException;

}
