package org.linkedon.client.utils;

import org.linkedon.client.exceptions.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HTTP {

    private static final String BASE_URL = "http://localhost:8080";
    public static HashMap<String, String> sendRequest(String path, String requestMethod, String authToken, String body) throws IOException, ApplicationException {
        URL url = new URL(BASE_URL + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);

        if (authToken != null) {
            connection.setRequestProperty("Authorization", STR."Bearer \{authToken}");
        }

        if (body != null) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes());
                os.flush();
            }
        }

        int responseCode = connection.getResponseCode();
        switch (responseCode) {
            case HttpURLConnection.HTTP_OK -> {}
            case HttpURLConnection.HTTP_FORBIDDEN -> throw new PermissionDeniedException();
            case HttpURLConnection.HTTP_BAD_REQUEST -> throw new BadRequestException();
            case HttpURLConnection.HTTP_NOT_FOUND -> throw new NotFoundException();
            case HttpURLConnection.HTTP_UNAUTHORIZED -> throw new UnauthorizedException();
            default -> throw new ApplicationException(500, "Unknown error");
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        HashMap<String, String> result = new HashMap<>();
        result.put("body", response.toString());
        result.put("authorization", connection.getHeaderField("Authorization"));

        return result;
    }

    public static HashMap<String, String> sendRequest(String path, String requestMethod, String authToken,String body, HashMap<String, String> params) throws IOException, ApplicationException {
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (query.length() != 0) {
                query.append("&");
            }
            query.append(encodeString(entry.getKey())).append("=").append(encodeString(entry.getValue()));
        }

        return sendRequest(STR."\{path}?\{query.toString()}", requestMethod, authToken, body);
    }

    public static HashMap<String, String> sendRequest(String path, String requestMethod, String authToken, File file) throws IOException, ApplicationException {
        URL url = new URL(BASE_URL + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);

        if (authToken != null) {
            connection.setRequestProperty("Authorization", STR."Bearer \{authToken}");
        }

        if (file != null) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "png");
            connection.setRequestProperty("Content-Length", String.valueOf(file.length()));

            try (FileInputStream fileInputStream = new FileInputStream(file);
                 OutputStream os = connection.getOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            }
        }

        int responseCode = connection.getResponseCode();
        switch (responseCode) {
            case HttpURLConnection.HTTP_OK -> {}
            case HttpURLConnection.HTTP_FORBIDDEN -> throw new PermissionDeniedException();
            case HttpURLConnection.HTTP_BAD_REQUEST -> throw new BadRequestException();
            case HttpURLConnection.HTTP_NOT_FOUND -> throw new NotFoundException();
            case HttpURLConnection.HTTP_UNAUTHORIZED -> throw new UnauthorizedException();
            default -> throw new ApplicationException(500, "Unknown error");
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        HashMap<String, String> result = new HashMap<>();
        result.put("body", response.toString());
        result.put("authorization", connection.getHeaderField("Authorization"));

        return result;
    }

    public static InputStream sendMediaRequest(String path, String requestMethod, String authToken) throws IOException, ApplicationException {
        URL url = new URL(BASE_URL + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);

        if (authToken != null) {
            connection.setRequestProperty("Authorization", "Bearer " + authToken);
        }

        int responseCode = connection.getResponseCode();
        switch (responseCode) {
            case HttpURLConnection.HTTP_OK -> {}
            case HttpURLConnection.HTTP_FORBIDDEN -> throw new PermissionDeniedException();
            case HttpURLConnection.HTTP_BAD_REQUEST -> throw new BadRequestException();
            case HttpURLConnection.HTTP_NOT_FOUND -> throw new NotFoundException();
            case HttpURLConnection.HTTP_UNAUTHORIZED -> throw new UnauthorizedException();
            default -> throw new ApplicationException(500, "Unknown error");
        }

        return connection.getInputStream();
    }

    public static String encodeString(String phrase) {
        try {
            return URLEncoder.encode(phrase, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
