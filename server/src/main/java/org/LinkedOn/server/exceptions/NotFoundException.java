package org.LinkedOn.server.exceptions;

public class NotFoundException extends ApplicationException{
    public NotFoundException()
    {
        super(404, "Not-Found");
    }
}
