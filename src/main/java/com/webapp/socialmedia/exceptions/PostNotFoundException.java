package com.webapp.socialmedia.exceptions;

public class PostNotFoundException extends BadRequestException{
    public PostNotFoundException(String message) {
        super(message);
    }
    public PostNotFoundException() {
        super("Bài viết không tồn tại");
    }
}
