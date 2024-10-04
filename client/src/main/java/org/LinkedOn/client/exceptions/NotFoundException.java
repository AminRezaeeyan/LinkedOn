package org.linkedon.client.exceptions;

public class NotFoundException extends ApplicationException{
    public NotFoundException()
    {
        super(404, "Not-Found");
    }
}
