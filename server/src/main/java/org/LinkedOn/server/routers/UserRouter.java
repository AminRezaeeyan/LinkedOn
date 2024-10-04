package org.LinkedOn.server.routers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.LinkedOn.server.controllers.EducationController;
import org.LinkedOn.server.controllers.PostController;
import org.LinkedOn.server.controllers.UserController;
import org.LinkedOn.server.exceptions.*;
import org.LinkedOn.server.models.ContactInfo;
import org.LinkedOn.server.models.User;
import org.LinkedOn.server.utils.JWTUtil;
import org.LinkedOn.server.utils.HTTP;

import java.io.IOException;
import java.sql.SQLException;


public class UserRouter implements HttpHandler {
    private static UserRouter instance;
    private final UserController userController;
    private final EducationController educationController;

    private UserRouter() {
        userController = UserController.getInstance();
        educationController = EducationController.getInstance();
    }

    public static UserRouter getInstance() {
        if (instance == null) instance = new UserRouter();
        return instance;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requesterId = null;
        try {
            requesterId = JWTUtil.verifyToken(exchange);
        } catch (UnauthorizedException e) {
            HTTP.sendResponse(exchange, 403, "Unauthorized");
            return;
        }

        try {
            switch (exchange.getRequestMethod()) {
                case "GET" -> handleGetRequest(exchange, requesterId);
                case "POST" -> handlePostRequest(exchange, requesterId);
                case "PUT" -> handlePutRequest(exchange, requesterId);
                case "DELETE" -> handleDeleteRequest(exchange, requesterId);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            HTTP.sendResponse(exchange, 500, "Internal Server Error");
        } catch (ApplicationException e) {
            HTTP.sendResponse(exchange, e.getStatusCode(), e.getMessage());
        }
    }

    public void handleGetRequest(HttpExchange exchange, String requesterId) throws SQLException, IOException, ApplicationException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        switch (pathSegments.length) {
            case 1 -> {  // users?searchQuery=:searchQuery
                String searchQuery = HTTP.getQueryParamValue(exchange, "searchQuery");
                String users = (searchQuery == null) ? userController.fetchUsers() : userController.searchUsers(searchQuery);
                HTTP.sendResponse(exchange, 200, users);
            }
            case 2 -> {
                switch (pathSegments[1]) {
                    case "checkEmail" -> { // users/checkEmail?email=:email
                        String email = HTTP.getQueryParamValue(exchange, "email");
                        if (email == null) throw new BadRequestException();
                        HTTP.sendResponse(exchange, 200, (userController.isEmailAvailable(email)) ? "Available" : "Conflict");
                    }
                    case "login" -> { // users/login?email=:email&password=:password
                        String email = HTTP.getQueryParamValue(exchange, "email");
                        String password = HTTP.getQueryParamValue(exchange, "password");
                        if (email == null || password == null) throw new BadRequestException();
                        String userId = userController.authenticateUser(email, password);
                        if (userId == null) throw new UnauthorizedException();
                        exchange.getResponseHeaders().set("Authorization", "Bearer " + JWTUtil.createToken(userId));
                        HTTP.sendResponse(exchange, 200, userId);
                    }
                    case "skills" -> { // users/skills
                        String skills = userController.fetchSkills();
                        HTTP.sendResponse(exchange, 200, skills);
                    }
                    default -> { // users/:id?detail=(full-brief)
                        if (!userController.userExists(pathSegments[1])) throw new NotFoundException();
                        String detail = HTTP.getQueryParamValue(exchange, "detail");
                        String user = userController.fetchUser(pathSegments[1], (detail == null || "full".equals(detail)));
                        HTTP.sendResponse(exchange, 200, user);

                    }
                }
            }
            case 3 -> {
                switch (pathSegments[2]) {
                    case "contactInfo" -> { // users/:id/contactInfo
                        if (!userController.userExists(pathSegments[1])) throw new NotFoundException();
                        String contactInfo = userController.fetchContactInfo(pathSegments[1]);
                        HTTP.sendResponse(exchange, 200, contactInfo);
                    }
                    case "educations" -> { // users/:id/educations
                        if (!userController.userExists(pathSegments[1])) throw new NotFoundException();
                        String educations = educationController.fetchUserEducations(pathSegments[1]);
                        HTTP.sendResponse(exchange, 200, educations);
                    }
                    case "skills" -> { // users/:id/skills
                        if (!userController.userExists(pathSegments[1])) throw new NotFoundException();
                        String skills = userController.fetchUserSkills(pathSegments[1]);
                        HTTP.sendResponse(exchange, 200, skills);
                    }
                    case "feed" -> { // users/:id/feed
                        if (!userController.userExists(pathSegments[1])) throw new NotFoundException();
                        String feed = PostController.getInstance().getUserFeed(pathSegments[1]);
                        HTTP.sendResponse(exchange, 200, feed);
                    }
                    default -> throw new BadRequestException();
                }
            }
            default -> throw new BadRequestException();
        }
    }

    public void handlePostRequest(HttpExchange exchange, String requesterId) throws SQLException, IOException, ApplicationException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        switch (pathSegments.length) {
            case 1 -> {  // users/
                User user = HTTP.deserializeExchangeBody(exchange, User.class);
                String userId = userController.addUser(user);
                String token = JWTUtil.createToken(userId);
                exchange.getResponseHeaders().set("Authorization", "Bearer " + token);
                HTTP.sendResponse(exchange, 200, userId);
            }
            case 3 -> {
                switch (pathSegments[2]) {
                    case "skills" -> { // users/:id/skills
                        if (!userController.userExists(pathSegments[1])) throw new NotFoundException();
                        String skill = HTTP.deserializeExchangeBody(exchange, String.class);
                        userController.addSkill(pathSegments[1], skill);
                        HTTP.sendResponse(exchange, 200, "Success");
                    }
                    default -> throw new BadRequestException();
                }
            }
            default -> throw new BadRequestException();
        }
    }

    public void handlePutRequest(HttpExchange exchange, String requesterId) throws SQLException, IOException, ApplicationException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        switch (pathSegments.length) {
            case 2 -> {  // users/:id
                User user = HTTP.deserializeExchangeBody(exchange, User.class);
                if (!user.getId().equals(requesterId)) throw new PermissionDeniedException();
                userController.updateUser(user);
                HTTP.sendResponse(exchange, 200, "Success");
            }
            case 3 -> {
                if ("contactInfo".equals(pathSegments[2])) {
                    // users/:id/contactInfo
                    if (!pathSegments[1].equals(requesterId)) throw new PermissionDeniedException();
                    ContactInfo contactInfo = HTTP.deserializeExchangeBody(exchange, ContactInfo.class);
                    userController.updateContactInfo(contactInfo);
                    HTTP.sendResponse(exchange, 200, "Success");
                }
            }
            default -> throw new BadRequestException();
        }
    }

    public void handleDeleteRequest(HttpExchange exchange, String requesterId) throws SQLException, IOException, ApplicationException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        switch (pathSegments.length) {
            case 2 -> {  // users/:id
                if (!pathSegments[1].equals(requesterId)) throw new PermissionDeniedException();
                userController.removeUser(pathSegments[1]);
                HTTP.sendResponse(exchange, 200, "Success");
            }
            case 3 -> {
                switch (pathSegments[2]) {
                    case "skills" -> { // users/:id/skills
                        if (!userController.userExists(pathSegments[1])) throw new NotFoundException();
                        String skill = HTTP.deserializeExchangeBody(exchange, String.class);
                        userController.removeSkill(pathSegments[1] , skill);
                        HTTP.sendResponse(exchange, 200, "Success");
                    }
                    default -> throw new BadRequestException();
                }
            }
            default -> throw new BadRequestException();
        }
    }
}
