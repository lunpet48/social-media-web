package com.webapp.socialmedia.exceptions;

public class UserNotMatchTokenException extends BadRequestException{
    public UserNotMatchTokenException() {
        super("User ID mismatch. Access denied");
    }
}
