package org.LinkedOn.server.routers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.LinkedOn.server.controllers.FollowController;
import org.LinkedOn.server.exceptions.*;
import org.LinkedOn.server.utils.HTTP;
import org.LinkedOn.server.utils.JWTUtil;

import java.io.IOException;
import java.sql.SQLException;

public class FollowRouter implements HttpHandler {
    private static FollowRouter instance;

    private final FollowController followController;

    private FollowRouter(){
        followController = FollowController.getInstance();
    }

    public static FollowRouter getInstance(){
        if (instance == null) instance = new FollowRouter();
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
                case "POST" -> handlePostRequest(exchange, requesterId);
                case "DELETE" -> handleDeleteRequest(exchange, requesterId);
                default -> throw new BadRequestException();
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

        if (pathSegments.length != 3) throw new BadRequestException();
        String userId = pathSegments[1];

        switch (pathSegments[2]){
            case "followers" ->{ //follows/:user-id/followers
                String followers = followController.getFollowers(userId);
                HTTP.sendResponse(exchange, 200, followers);
            }
            case "followings" ->{ //follows/:user-id/followings
                String followings = followController.getFollowings(userId);
                HTTP.sendResponse(exchange, 200, followings);
            }
            default -> throw new BadRequestException();
        }
    }

    public void handlePostRequest(HttpExchange exchange, String requesterId) throws SQLException, IOException, ApplicationException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        if (pathSegments.length != 4 || !pathSegments[2].equals("followers")) {
            throw new BadRequestException();
        }
        String followerId = pathSegments[3];
        String followedId = pathSegments[1];

        if (!requesterId.equals(followerId)) throw new PermissionDeniedException();

        followController.addFollow(followerId, followedId);
        HTTP.sendResponse(exchange, 200, "Processed");
    }

    public void handleDeleteRequest(HttpExchange exchange, String requesterId) throws SQLException, IOException, ApplicationException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        if (pathSegments.length != 4 || !pathSegments[2].equals("followers"))
            throw new BadRequestException();

        String followerId = pathSegments[3];
        String followedId = pathSegments[1];

        if (!requesterId.equals(followerId) && !requesterId.equals(followedId))
            throw new PermissionDeniedException();

        followController.removeFollow(followerId, followedId);
        HTTP.sendResponse(exchange, 200, "Processed");
    }
}
