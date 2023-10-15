package com.webapp.socialmedia.exceptions;

public class RelationshipNotFoundException extends BadRequestException {
    public RelationshipNotFoundException(String message) {
        super(message);
    }
}
