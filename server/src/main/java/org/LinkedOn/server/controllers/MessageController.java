package org.LinkedOn.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.LinkedOn.server.DAO.MessageDAO;
import org.LinkedOn.server.DAO.impl.MessageDAOImpl;
import org.LinkedOn.server.models.Message;
import org.LinkedOn.server.utils.Json;

import java.sql.SQLException;
import java.util.ArrayList;

public class MessageController {
    private static MessageController instance;
    private final MessageDAO messageDAO;

    private MessageController() {
        this.messageDAO = MessageDAOImpl.getInstance();
    }

    public synchronized static MessageController getInstance() {
        if (instance == null) instance = new MessageController();
        return instance;
    }

    public synchronized void addMessage(Message message) throws SQLException {
        messageDAO.addMessage(message);
    }

    public synchronized String getMessages(String userId1, String userId2) throws SQLException, JsonProcessingException {
        ArrayList<Message> chat = messageDAO.getMessages(userId1, userId2);
        return Json.toJsonArray(chat);
    }
}
