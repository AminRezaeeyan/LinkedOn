package org.LinkedOn.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.LinkedOn.server.DAO.ConnectDAO;
import org.LinkedOn.server.DAO.impl.ConnectDAOImpl;
import org.LinkedOn.server.models.Connect;
import org.LinkedOn.server.models.User;
import org.LinkedOn.server.utils.Json;

import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectController {
    private static ConnectController instance;
    private final ConnectDAO connectDAO;

    private ConnectController(){
        connectDAO = ConnectDAOImpl.getInstance();
    }

    public static synchronized ConnectController getInstance() {
        if (instance == null) instance = new ConnectController();
        return instance;
    }

    public synchronized void requestConnection(Connect connect) throws SQLException {
        if (connectDAO.connectionExists(connect.getUserId1(), connect.getUserId2())) return;
        connect.setStatus(Connect.ConnectionStatus.PENDING);
        connectDAO.addConnection(connect);
    }

    public synchronized void acceptConnection(Connect connect) throws SQLException {
        connect.setStatus(Connect.ConnectionStatus.CONNECTED);
        connectDAO.updateConnection(connect);
    }

    public synchronized String getConnections(String userId) throws SQLException, JsonProcessingException {
        ArrayList<User> connections = connectDAO.getConnections(userId);
        return Json.toJsonArrayWithView(connections, Json.Views.Brief.class);
    }

    public synchronized String getConnectionRequests(String userId) throws SQLException, JsonProcessingException {
        ArrayList<User> connectionRequests = connectDAO.getConnectionRequests(userId);
        return Json.toJsonArrayWithView(connectionRequests, Json.Views.Brief.class);
    }

    public synchronized void removeConnection(String userId1, String userId2) throws SQLException {
        connectDAO.removeConnection(userId1, userId2);
    }
}
