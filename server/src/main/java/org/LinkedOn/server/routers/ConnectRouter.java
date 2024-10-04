package org.LinkedOn.server.routers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.LinkedOn.server.controllers.ConnectController;
import org.LinkedOn.server.exceptions.*;
import org.LinkedOn.server.models.Connect;
import org.LinkedOn.server.utils.HTTP;
import org.LinkedOn.server.utils.JWTUtil;

import java.io.IOException;
import java.sql.SQLException;

public class ConnectRouter implements HttpHandler {
    private static ConnectRouter instance;
    private ConnectController connectController;

    private ConnectRouter(){
        connectController = ConnectController.getInstance();
    }

    public synchronized static ConnectRouter getInstance(){
        if (instance == null) instance = new ConnectRouter();
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

        String userId = pathSegments[1];

        switch (pathSegments.length) {
            case 2 -> {//connects/:user-id
                String connections = connectController.getConnections(userId);
                HTTP.sendResponse(exchange, 200, connections);

            }
            case 3 -> {
                switch (pathSegments[2]) {
                    case "requests" -> { //connects/:user-id/requests
                        String receivedRequests = connectController.getConnectionRequests(userId);
                        HTTP.sendResponse(exchange, 200, receivedRequests);
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

        if (pathSegments.length != 4) throw new BadRequestException();

        String userId1 = pathSegments[1];
        String userId2 = pathSegments[3];

        switch (pathSegments[2]) {
            case "request" -> { //connects/:userId1/request/:userId2
//                if (!requesterId.equals(userId1)) throw new PermissionDeniedException();
                connectController.requestConnection(new Connect(userId1, userId2));
                HTTP.sendResponse(exchange, 200, "Processed");
            }
            case "accept" -> { //connects/:userId1/accept/:userId2
//                if (!requesterId.equals(userId1)) throw new PermissionDeniedException();
                connectController.acceptConnection(new Connect(userId1, userId2));
                HTTP.sendResponse(exchange, 200, "Processed");
            }
            default -> throw new BadRequestException();
        }
    }

    public void handleDeleteRequest(HttpExchange exchange, String requesterId) throws SQLException, ApplicationException, IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        if (pathSegments.length != 3) throw new BadRequestException();  //connects/:userId1/:userId2

        String userId1 = pathSegments[1];
        String userId2 = pathSegments[2];

        if (!requesterId.equals(userId1) && !requesterId.equals(userId2))
            throw new PermissionDeniedException();

        connectController.removeConnection(userId1, userId2);
        HTTP.sendResponse(exchange, 200, "Processed");
    }
}
