package com.webapp.socialmedia.exceptions;

public class UserExistException extends BadRequestException {
    public UserExistException() {
        super();
    }
    public UserExistException(String message, Throwable cause) {
        super(message, cause);
    }
    public UserExistException(String message) {
        super(message);
    }
    public UserExistException(Throwable cause) {
        super(cause);
    }
}
