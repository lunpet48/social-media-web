package com.webapp.socialmedia.exceptions;

public class UserNotFoundException extends ResourceNotFoundException{
    public UserNotFoundException() {
        super("User Not Found");
    }
}
