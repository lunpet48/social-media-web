package com.webapp.socialmedia.exceptions;

public class UserNotFoundException extends BadRequestException{
    public UserNotFoundException() {
        super("User Not Found");
    }
}
