package org.LinkedOn.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.LinkedOn.server.DAO.ContactInfoDAO;
import org.LinkedOn.server.DAO.SkillDAO;
import org.LinkedOn.server.DAO.UserDAO;
import org.LinkedOn.server.DAO.impl.ContactInfoDAOImpl;
import org.LinkedOn.server.DAO.impl.SkillDAOImpl;
import org.LinkedOn.server.DAO.impl.UserDAOImpl;
import org.LinkedOn.server.models.ContactInfo;
import org.LinkedOn.server.models.User;
import org.LinkedOn.server.utils.Hash;
import org.LinkedOn.server.utils.Json;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserController {
    private static UserController instance;
    private final UserDAO userDAO;
    private final ContactInfoDAO contactInfoDAO;
    private final SkillDAO skillDAO;

    private UserController() {
        userDAO = UserDAOImpl.getInstance();
        contactInfoDAO = ContactInfoDAOImpl.getInstance();
        skillDAO = SkillDAOImpl.getInstance();
    }

    public static synchronized UserController getInstance() {
        if (instance == null) instance = new UserController();
        return instance;
    }

    public synchronized String addUser(User user) throws SQLException {
        return userDAO.addUser(user);
    }

    public synchronized String fetchUser(String id, boolean fullInfo) throws SQLException, JsonProcessingException {
        User user = userDAO.fetchUserById(id);
        if (fullInfo)
            return Json.toJsonWithView(user, Json.Views.Full.class);
        else
            return Json.toJsonWithView(user, Json.Views.Brief.class);
    }

    public synchronized String fetchUsers() throws SQLException, JsonProcessingException {
        ArrayList<User> users = userDAO.fetchUsers();
        return Json.toJsonArray(users);
    }

    public synchronized String authenticateUser(String email, String password) throws SQLException {
        User user = userDAO.fetchUserByEmail(email);
        if (user == null || !Hash.verifyPassword(password, user.getPassword())) return null;
        return user.getId();
    }

    public synchronized String searchUsers(String searchQuery) throws SQLException, JsonProcessingException {
        ArrayList<User> users = userDAO.searchUsers(searchQuery, 6);
        return Json.toJsonArrayWithView(users, Json.Views.Brief.class);
    }

    public synchronized void updateUser(User user) throws SQLException {
        userDAO.updateUser(user);
    }

    public synchronized void removeUser(String id) throws SQLException {
        userDAO.removeUser(id);
    }

    public synchronized boolean userExists(String id) throws SQLException {
        return userDAO.userExists(id);
    }

    public synchronized boolean isEmailAvailable(String email) throws SQLException {
        return !userDAO.emailExists(email);
    }

    public synchronized String fetchContactInfo(String userId) throws SQLException, JsonProcessingException {
        ContactInfo contactInfo = contactInfoDAO.fetchContactInfo(userId);
        return Json.toJson(contactInfo);
    }

    public synchronized void updateContactInfo(ContactInfo contactInfo) throws SQLException {
        contactInfoDAO.updateContactInfo(contactInfo);
    }

    public synchronized void addSkill(String userId, String skill) throws SQLException {
        skillDAO.addSkill(userId, skill);
    }

    public synchronized String fetchUserSkills(String userId) throws SQLException, JsonProcessingException {
        ArrayList<String> skills = skillDAO.fetchUserSkills(userId);
        return Json.toJsonArray(skills);
    }

    public synchronized String fetchSkills() throws SQLException, JsonProcessingException {
        ArrayList<String> skills = skillDAO.fetchSkills();
        return Json.toJsonArray(skills);
    }

    public synchronized void removeSkill(String userId, String skill) throws SQLException {
        skillDAO.removeSkill(userId, skill);
    }
}
