package com.webapp.socialmedia.config;

import com.webapp.socialmedia.dto.WrappingResponse;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.dto.responses.ValidationErrorResponse;
import com.webapp.socialmedia.dto.responses.Violation;
import com.webapp.socialmedia.exceptions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @WrappingResponse(status = HttpStatus.BAD_REQUEST, success = false)
    public Object handleBadCredentialsException(BadCredentialsException e) {
        return "Tài khoản hoặc mật khẩu không chính xác";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @WrappingResponse(status = HttpStatus.NOT_FOUND, success = false)
    public Object handleBadCredentialsException(ResourceNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @WrappingResponse(status = HttpStatus.BAD_REQUEST, success = false)
    public Object UsernameNotFoundException(UsernameNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(BadRequestException.class)
    @WrappingResponse(status = HttpStatus.BAD_REQUEST, success = false)
    public Object BadRequestException(BadRequestException e) {
        return e.getMessage();
    }

//    @ExceptionHandler(Exception.class)
//    @WrappingResponse(status = HttpStatus.INTERNAL_SERVER_ERROR, success = false)
    public Object handleUnwantedException(Exception e) {
        return e;
    }

    @ExceptionHandler(PostNotFoundException.class)
    @WrappingResponse(status = HttpStatus.NOT_FOUND, success = false)
    public Object handlePostNotFoundException(PostNotFoundException e) {return e.getMessage();}

    @ExceptionHandler(PostCannotUploadException.class)
    @WrappingResponse(status = HttpStatus.BAD_REQUEST, success = false)
    public Object handlePostCannotUploadException(PostCannotUploadException e) {return e.getMessage();}

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse(e.getMessage());
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.getErrors().add(
                    new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return error;
    }

    @ExceptionHandler(UserLockedException.class)
    @ResponseBody
    public ResponseEntity<?> handleUserLockedException(UserLockedException e) {
        Map<String, String> map = new HashMap<>();
        map.put("logId", e.getMessage());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setError(true);
        responseDTO.setMessage("Tài khoản bị khóa");
        responseDTO.setData(map);
        return ResponseEntity.status(HttpStatus.LOCKED).body(responseDTO);
    }
}
