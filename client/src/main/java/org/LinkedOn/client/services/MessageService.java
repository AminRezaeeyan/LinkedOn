package org.linkedon.client.services;

import org.linkedon.client.Client;
import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.Message;
import org.linkedon.client.utils.HTTP;
import org.linkedon.client.utils.Json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MessageService {
    private static MessageService instance;

    private MessageService(){}

    public static MessageService getInstance(){
        if (instance == null) instance = new MessageService();
        return instance;
    }

    public void sendMessage(Message message) throws ApplicationException, IOException {
        HTTP.sendRequest(STR."/messages/\{message.getSenderId()}/\{message.getReceiverId()}", "POST", Client.getToken(), Json.toJson(message));
    }

    public ArrayList<Message> getMessages(String userId1, String userId2) throws ApplicationException, IOException {
        HashMap<String, String> response = HTTP.sendRequest(STR."/messages/\{userId1}/\{userId2}", "GET", Client.getToken(), (String) null);
        String chatJson = response.get("body");
        return (ArrayList<Message>) Json.fromJsonArray(chatJson, Message[].class);
    }
}
