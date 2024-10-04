package org.LinkedOn.server.exceptions;

public class PermissionDeniedException extends ApplicationException {
    public PermissionDeniedException() {
        super(403, "Permission-Denied");
    }
}