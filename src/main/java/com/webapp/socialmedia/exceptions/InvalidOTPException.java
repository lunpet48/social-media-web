package com.webapp.socialmedia.exceptions;

public class InvalidOTPException extends BadRequestException{
    public InvalidOTPException() {
        super("Mã OTP Không hợp lệ");
    }
}
