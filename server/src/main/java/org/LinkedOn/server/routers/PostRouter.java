package org.LinkedOn.server.routers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.LinkedOn.server.controllers.PostController;
import org.LinkedOn.server.exceptions.*;
import org.LinkedOn.server.models.Post;
import org.LinkedOn.server.utils.HTTP;
import org.LinkedOn.server.utils.JWTUtil;

import java.io.IOException;
import java.sql.SQLException;

public class PostRouter implements HttpHandler {
    private static PostRouter instance;
    private final PostController postController;

    private PostRouter() {
        this.postController = PostController.getInstance();
    }

    public synchronized static PostRouter getInstance() {
        if (instance == null) instance = new PostRouter();
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


    public void handlePostRequest(HttpExchange exchange, String requesterId) throws SQLException, IOException, ApplicationException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        switch (pathSegments.length) {
            case 1 -> {  // posts/
                Post post = HTTP.deserializeExchangeBody(exchange, Post.class);
                String postId = postController.addPost(post);
                HTTP.sendResponse(exchange, 200, postId);
            }
            case 4 -> {  // posts/:id/likes/:user_id
                postController.addLike(pathSegments[3], pathSegments[1]);
                HTTP.sendResponse(exchange, 200, "Processed");
            }
            default -> throw new BadRequestException();
        }
    }
}
