package com.webapp.socialmedia.exceptions;

public class UserLockedException extends RuntimeException{
    public UserLockedException() {super();}
    public UserLockedException(String logId) {super(logId);}
}
