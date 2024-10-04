package org.LinkedOn.server.exceptions;

public class BadRequestException extends ApplicationException {
    public BadRequestException() {
        super(400, "Bad-Request");
    }
}