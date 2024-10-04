package org.LinkedOn.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HTTP {
    public static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    public static void sendResponse(HttpExchange exchange, int statusCode, File file, String fileType) throws IOException {
        exchange.sendResponseHeaders(statusCode, file.length());
        exchange.getResponseHeaders().put("Content-Type", Collections.singletonList(fileType));
        try (OutputStream os = exchange.getResponseBody()) {
            Files.copy(file.toPath(), os);
        }
    }

    public static <T> T deserializeExchangeBody(HttpExchange exchange, Class<T> clazz) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        return Json.fromJson(inputStream, clazz);
    }

    public static String getQueryParamValue(HttpExchange exchange, String key) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> queryParams = queryToMap(query);
        return queryParams.getOrDefault(key, null);
    }

    private static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], entry[1]);
                } else {
                    result.put(entry[0], "");
                }
            }
        }
        return result;
    }

    public static boolean isPublicEndPoint(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (path.equals("/users") && method.equals("POST")){
            return true;
        }

        if (path.equals("/users/login") || path.equals("/users/checkEmail")){
            return true;
        }

        return false;
    }
}
