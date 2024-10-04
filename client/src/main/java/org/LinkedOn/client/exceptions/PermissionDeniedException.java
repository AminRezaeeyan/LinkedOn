package org.linkedon.client.exceptions;

public class PermissionDeniedException extends ApplicationException {
    public PermissionDeniedException() {
        super(403, "Permission-Denied");
    }
}