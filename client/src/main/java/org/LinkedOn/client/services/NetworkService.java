package org.linkedon.client.services;

import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.User;
import org.linkedon.client.utils.HTTP;
import org.linkedon.client.utils.Json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NetworkService {
    private static NetworkService instance;

    private NetworkService(){}

    public static NetworkService getInstance(){
        if (instance == null) instance = new NetworkService();
        return instance;
    }

    public ArrayList<User> fetchUserConnections(String userId, String authToken) throws ApplicationException, IOException {
        HashMap<String, String> response = HTTP.sendRequest(STR."/connects/\{userId}","GET", authToken, (String) null);
        String usersJson = response.get("body");
        return (ArrayList<User>) Json.fromJsonArray(usersJson, User[].class);
    }

    public ArrayList<User> fetchRequests(String userId, String authToken) throws ApplicationException, IOException {
        HashMap<String, String> response = HTTP.sendRequest(STR."/connects/\{userId}/requests","GET", authToken, (String) null);
        String usersJson = response.get("body");
        return (ArrayList<User>) Json.fromJsonArray(usersJson, User[].class);
    }

    public void request(String requesterId, String acceptorId, String authToken) throws ApplicationException, IOException {
        HTTP.sendRequest(STR."/connects/\{requesterId}/request/\{acceptorId}","POST", authToken, (String) null);
    }

    public void accept(String acceptorId, String requesterId, String authToken) throws ApplicationException, IOException {
        HTTP.sendRequest(STR."/connects/\{acceptorId}/accept/\{requesterId}","POST", authToken, (String) null);
    }

    public void reject(String rejecterId, String requesterId, String authToken) throws ApplicationException, IOException {
        HTTP.sendRequest(STR."/connects/\{rejecterId}/\{requesterId}","DELETE", authToken, (String) null);
    }

    public ArrayList<User> fetchFollowers(String userId, String authToken) throws ApplicationException, IOException {
        HashMap<String, String> response = HTTP.sendRequest(STR."/follows/\{userId}/followers","GET", authToken, (String) null);
        String usersJson = response.get("body");
        return (ArrayList<User>) Json.fromJsonArray(usersJson, User[].class);
    }

    public ArrayList<User> fetchFollowings(String userId, String authToken) throws ApplicationException, IOException {
        HashMap<String, String> response = HTTP.sendRequest(STR."/follows/\{userId}/followings","GET", authToken, (String) null);
        String usersJson = response.get("body");
        return (ArrayList<User>) Json.fromJsonArray(usersJson, User[].class);
    }

    public void follow(String followerId, String followedId, String authToken) throws ApplicationException, IOException {
        HTTP.sendRequest(STR."/follows/\{followedId}/followers/\{followerId}","POST", authToken, (String) null);
    }
}
