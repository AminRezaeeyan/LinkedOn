package org.LinkedOn.server.routers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.LinkedOn.server.exceptions.*;
import org.LinkedOn.server.utils.HTTP;
import org.LinkedOn.server.utils.JWTUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class MediaRouter implements HttpHandler {
    private static final String USERS_MEDIA_PATH = "src/main/java/org/LinkedOn/server/media/users/";
    private static final String POSTS_MEDIA_PATH = "src/main/java/org/LinkedOn/server/media/posts/";

    private static MediaRouter instance;

    private MediaRouter() {
    }

    public static MediaRouter getInstance() {
        if (instance == null) instance = new MediaRouter();
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
                // Use POST method for updating too
                case "DELETE" -> handleDeleteRequest(exchange);
                default -> throw new BadRequestException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            HTTP.sendResponse(exchange, 500, "Internal Server Error");
        } catch (ApplicationException e) {
            HTTP.sendResponse(exchange, e.getStatusCode(), e.getMessage());
        }
    }

    public void handleGetRequest(HttpExchange exchange) throws ApplicationException, IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        if (pathSegments.length != 5) throw new BadRequestException();
        String mediaName = pathSegments[3];
        String mediaExtension = pathSegments[4];

        switch (pathSegments[1]) {
            case "users" -> { //media/users/:id/mediaName/mediaExtension
                String userId = pathSegments[2];
                File file = new File(USERS_MEDIA_PATH + userId + "/" + mediaName + "." + mediaExtension);
                if (!file.exists()) throw new NotFoundException();
                HTTP.sendResponse(exchange, 200, file, pathSegments[4]);
            }
            case "posts" -> { //media/posts/:id/mediaName/mediaExtension
                String postId = pathSegments[2];
                File file = new File(POSTS_MEDIA_PATH + postId + "/" + mediaName + "." + mediaExtension);
                if (!file.exists()) throw new NotFoundException();
                HTTP.sendResponse(exchange, 200, file, pathSegments[4]);
            }
            default -> throw new BadRequestException();
        }
    }

    public void handlePostRequest(HttpExchange exchange) throws ApplicationException, IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        if (pathSegments.length != 5) throw new BadRequestException();
        String mediaName = pathSegments[3];
        String mediaExtension = pathSegments[4];

        switch (pathSegments[1]) {
            case "users" -> { //media/users/:id/mediaName/mediaExtension
                String userId = pathSegments[2];
                Path targetPath = Path.of(USERS_MEDIA_PATH, userId);
                Files.createDirectories(targetPath);
                Files.copy(exchange.getRequestBody(), targetPath.resolve(mediaName + "." + mediaExtension), StandardCopyOption.REPLACE_EXISTING);
                exchange.getRequestBody().close();
                HTTP.sendResponse(exchange, 200, "Success");

            }
            case "posts" -> { //media/posts/:id/mediaName/mediaExtension
                String postId = pathSegments[2];
                Path targetPath = Path.of(POSTS_MEDIA_PATH, postId);
                Files.createDirectories(targetPath); // Create the directory if it doesn't exist
                Files.copy(exchange.getRequestBody(), targetPath.resolve(mediaName + "." + mediaExtension), StandardCopyOption.REPLACE_EXISTING);
                exchange.getRequestBody().close();
                HTTP.sendResponse(exchange, 200, "Success");
            }
            default -> throw new BadRequestException();
        }
    }

    public void handleDeleteRequest(HttpExchange exchange) throws ApplicationException, IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.substring(1).split("/");

        if (pathSegments.length != 5) throw new BadRequestException();
        String mediaName = pathSegments[3];
        String mediaExtension = pathSegments[4];

        switch (pathSegments[1]) {
            case "users" -> { //media/users/:id/mediaName/mediaExtension
                String userId = pathSegments[2];
                File file = new File(USERS_MEDIA_PATH + userId + "/" + mediaName + "." + mediaExtension);
                if (!file.exists()) throw new NotFoundException();
                file.delete();
                HTTP.sendResponse(exchange, 200, "Success");
            }
            case "posts" -> { //media/posts/:id/mediaName/mediaExtension
                String postId = pathSegments[2];
                File file = new File(POSTS_MEDIA_PATH + postId + "/" + mediaName + "." + mediaExtension);
                if (!file.exists()) throw new NotFoundException();
                file.delete();
                HTTP.sendResponse(exchange, 200, "Success");
            }
            default -> throw new BadRequestException();
        }
    }
}
