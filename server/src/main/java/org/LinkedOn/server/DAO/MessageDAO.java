package org.LinkedOn.server.DAO;

import org.LinkedOn.server.models.Message;

import java.sql.SQLException;
import java.util.ArrayList;

public interface MessageDAO {
    void addMessage(Message message) throws SQLException;
    ArrayList<Message> getMessages(String userId1, String userId2) throws SQLException;
}
