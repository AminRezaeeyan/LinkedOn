package org.linkedon.client.services;

import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.ContactInfo;
import org.linkedon.client.models.User;
import org.linkedon.client.utils.HTTP;
import org.linkedon.client.utils.Json;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class UserService {
    private static UserService instance;

    private UserService(){}

    public static UserService getInstance(){
        if (instance == null) instance = new UserService();
        return instance;
    }

    public HashMap<String, String> addUser(User user) throws ApplicationException, IOException {
        String userJson = Json.toJson(user);
        return HTTP.sendRequest("/users", "POST", null, userJson);
    }

    public void updateUser(User user, String authToken) throws ApplicationException, IOException {
        String userJson = Json.toJson(user);
        HTTP.sendRequest(STR."/users/\{user.getId()}", "PUT", authToken, userJson);
    }

    public ContactInfo fetchContactInfo(String id, String authToken) throws ApplicationException, IOException {
        HashMap<String, String> response = HTTP.sendRequest(STR."/users/\{id}/contactInfo","GET", authToken, (String) null);
        String json = response.get("body");
        return Json.fromJson(json, ContactInfo.class);
    }

    public void updateContactInfo(ContactInfo contactInfo, String authToken) throws ApplicationException, IOException {
        HTTP.sendRequest(STR."/users/\{contactInfo.getUserId()}/contactInfo","PUT", authToken, Json.toJson(contactInfo));
    }

    public User fetchUserFullInfo(String id, String authToken) throws ApplicationException, IOException {
        HashMap<String, String> queries = new HashMap<>();
        queries.put("detail", "full");
        HashMap<String, String> response = HTTP.sendRequest(STR."/users/\{id}","GET", authToken, null, queries);
        String userJson = response.get("body");
        return Json.fromJson(userJson, User.class);
    }

    public User fetchUserBasicInfo(String id, String authToken) throws ApplicationException, IOException {
        HashMap<String, String> queries = new HashMap<>();
        queries.put("detail", "brief");
        HashMap<String, String> response = HTTP.sendRequest(STR."/users/\{id}","GET", authToken, null, queries);
        String userJson = response.get("body");
        return Json.fromJson(userJson, User.class);
    }

    public ArrayList<User> searchUsers(String searchQuery, String authToken) throws ApplicationException, IOException {
        HashMap<String, String> queries = new HashMap<>();
        queries.put("searchQuery", searchQuery);
        HashMap<String, String> response = HTTP.sendRequest("/users","GET", authToken, null, queries);
        String usersJson = response.get("body");
        return (ArrayList<User>) Json.fromJsonArray(usersJson, User[].class);
    }

    public HashMap<String, String> authenticateUser(String email, String password) throws ApplicationException, IOException {
        HashMap<String, String> queries = new HashMap<>();
        queries.put("email", email);
        queries.put("password", password);
        return HTTP.sendRequest("/users/login","GET",null, null, queries);
    }

    public boolean isEmailAvailable(String email) throws ApplicationException, IOException {
        HashMap<String, String> queries = new HashMap<>();
        queries.put("email", email);
        HashMap<String, String> response = HTTP.sendRequest("/users/checkEmail","GET",null, null, queries);
        return "Available".equals(response.get("body"));
    }

    public InputStream fetchUserProfileImage(String userId, String authToken) throws IOException, ApplicationException {
        return HTTP.sendMediaRequest(STR."/media/users/\{userId}/profile/png", "GET", authToken);
    }

    public void addUserProfileImage(String userId, String authToken, File image) throws IOException, ApplicationException {
        HTTP.sendRequest(STR."/media/users/\{userId}/profile/png", "POST", authToken, image);
    }

    public InputStream fetchUserHeaderImage(String userId, String authToken) throws IOException, ApplicationException {
        return HTTP.sendMediaRequest(STR."/media/users/\{userId}/header/png", "GET", authToken);
    }

    public void addUserHeaderImage(String userId, String authToken, File image) throws IOException, ApplicationException {
        HTTP.sendRequest(STR."/media/users/\{userId}/header/png", "POST", authToken, image);
    }

    public ArrayList<String> fetchSkills(String authToken) throws IOException, ApplicationException {
        HashMap<String, String> response = HTTP.sendRequest("/users/skills", "GET", authToken, (String) null);
        String skills = response.get("body");
        return (ArrayList<String>) Json.fromJsonArray(skills, String[].class);
    }

    public ArrayList<String> fetchUserSkills(String userId, String authToken) throws IOException, ApplicationException {
        HashMap<String, String> response = HTTP.sendRequest(STR."/users/\{userId}/skills", "GET", authToken, (String) null);
        String skills = response.get("body");
        return (ArrayList<String>) Json.fromJsonArray(skills, String[].class);
    }

    public void addSkill(String skill, String userId, String authToken) throws IOException, ApplicationException {
        HTTP.sendRequest(STR."/users/\{userId}/skills", "POST", authToken, Json.toJson(skill));
    }
}
