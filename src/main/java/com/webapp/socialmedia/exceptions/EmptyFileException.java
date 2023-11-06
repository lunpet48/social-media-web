package com.webapp.socialmedia.exceptions;

public class EmptyFileException  extends BadRequestException{
    public EmptyFileException() {
        super("File Required");
    }
}
