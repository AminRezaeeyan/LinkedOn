package org.linkedon.client.exceptions;

public class UnauthorizedException extends ApplicationException {
    public UnauthorizedException() {
        super(401, "Unauthorized");
    }
}