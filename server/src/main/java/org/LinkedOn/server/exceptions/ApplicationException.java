package org.LinkedOn.server.exceptions;

public class ApplicationException extends Exception {
    private final int statusCode;

    public ApplicationException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}