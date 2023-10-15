package com.webapp.socialmedia.exceptions;

public class UserExistException extends BadRequestException {
    public UserExistException(String message) {
        super(message);
    }
}
