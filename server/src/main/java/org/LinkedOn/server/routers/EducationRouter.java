package org.LinkedOn.server.routers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.LinkedOn.server.controllers.EducationController;
import org.LinkedOn.server.exceptions.*;
import org.LinkedOn.server.models.Education;
import org.LinkedOn.server.utils.JWTUtil;
import org.LinkedOn.server.utils.HTTP;

import java.io.IOException;
import java.sql.SQLException;


public class EducationRouter implements HttpHandler {
    private static EducationRouter instance;
    private final EducationController educationController;

    private EducationRouter() {
        educationController = EducationController.getInstance();
    }

    public static EducationRouter getInstance() {
        if (instance == null) instance = new EducationRouter();
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
                case "GET" -> handleGetRequest(exchange);
                case "POST" -> handlePostRequest(exchange);
                case "PUT" -> handlePutRequest(exchange, requesterId);
                case "DELETE" -> handleDeleteRequest(exchange);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            HTTP.sendResponse(exchange, 500, "Internal Server Error");
        } catch (ApplicationException e) {
            HTTP.sendResponse(exchange, e.getStatusCode(), e.getMessage());
        }
    }

    public void handleGetRequest(HttpExchange exchange) throws SQLException, IOException, ApplicationException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        switch (pathSegments.length) {
            case 1 -> {  // educations?searchInstitute=:searchQuery
                String searchInstitute = HTTP.getQueryParamValue(exchange, "searchInstitute");
                String users = educationController.searchInstituteUsers(Education.Institute.valueOf(searchInstitute));
                HTTP.sendResponse(exchange, 200, users);
            }
            case 2 -> { // educations/:id
                    String education = educationController.fetchEducation(pathSegments[1]);
                    HTTP.sendResponse(exchange, 200, education);
            }
            default -> throw new BadRequestException();
        }
    }

    public void handlePostRequest(HttpExchange exchange) throws SQLException, IOException, ApplicationException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        switch (pathSegments.length) {
            case 1 -> {  // educations/
                Education education = HTTP.deserializeExchangeBody(exchange, Education.class);
                String educationId = educationController.addEducation(education);
                HTTP.sendResponse(exchange, 200, educationId);
            }
            default -> throw new BadRequestException();
        }
    }

    public void handlePutRequest(HttpExchange exchange, String requesterId) throws SQLException, IOException, ApplicationException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        switch (pathSegments.length) {
            case 2 -> {  // educations/:id
                Education education = HTTP.deserializeExchangeBody(exchange, Education.class);
                if (!education.getUserId().equals(requesterId)) throw new PermissionDeniedException();
                educationController.updateEducation(education);
                HTTP.sendResponse(exchange, 200, "Success");
            }
            default -> throw new BadRequestException();
        }
    }

    public void handleDeleteRequest(HttpExchange exchange) throws SQLException, IOException, ApplicationException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        switch (pathSegments.length) {
            case 2 -> {  // educations/:id
                educationController.removeEducation(pathSegments[1]);
                HTTP.sendResponse(exchange, 200, "Success");
            }
            default -> throw new BadRequestException();
        }
    }
}
