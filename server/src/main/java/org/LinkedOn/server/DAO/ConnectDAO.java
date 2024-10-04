package org.LinkedOn.server.DAO;

import org.LinkedOn.server.models.Connect;
import org.LinkedOn.server.models.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ConnectDAO {
    void addConnection(Connect connect) throws SQLException;

    void updateConnection(Connect connect) throws SQLException;

    ArrayList<User> getConnections(String userId) throws SQLException;

    ArrayList<User> getConnectionRequests(String userId) throws SQLException;

    void removeConnection(String userId1, String userId2) throws SQLException;

    boolean connectionExists(String userId1, String userId2) throws SQLException;
}
