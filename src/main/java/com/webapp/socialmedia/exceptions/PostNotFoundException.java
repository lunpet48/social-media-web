package com.webapp.socialmedia.exceptions;

public class PostNotFoundException extends ResourceNotFoundException{
    public PostNotFoundException(String message) {
        super(message);
    }
    public PostNotFoundException() {
        super("Bài viết không tồn tại");
    }
}
