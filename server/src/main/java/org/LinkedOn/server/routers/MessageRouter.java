package org.LinkedOn.server.routers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.LinkedOn.server.controllers.MessageController;
import org.LinkedOn.server.exceptions.*;
import org.LinkedOn.server.models.Message;
import org.LinkedOn.server.utils.HTTP;
import org.LinkedOn.server.utils.JWTUtil;

import java.io.IOException;
import java.sql.SQLException;

public class MessageRouter implements HttpHandler {
    private static MessageRouter instance;
    private final MessageController messageController;

    private MessageRouter(){
        this.messageController = MessageController.getInstance();
    }

    public static MessageRouter getInstance(){
        if (instance == null) instance = new MessageRouter();
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
                default -> throw new BadRequestException();
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
            case 3 -> {  // messages/:userId1/:userId2
                String chat = messageController.getMessages(pathSegments[1], pathSegments[2]);
                HTTP.sendResponse(exchange, 200, chat);
            }
            default -> throw new BadRequestException();
        }
    }

    public void handlePostRequest(HttpExchange exchange, String requesterId) throws SQLException, IOException, ApplicationException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        switch (pathSegments.length) {
            case 3 -> {  // messages/:senderId/:receiverId
                if (!requesterId.equals(pathSegments[1])) throw new PermissionDeniedException();
                Message message = HTTP.deserializeExchangeBody(exchange, Message.class);
                messageController.addMessage(message);
                HTTP.sendResponse(exchange, 200, "Processed");
            }
            default -> throw new BadRequestException();
        }
    }
}
