package com.webapp.socialmedia.dto;

import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WrappingResponse {
    public HttpStatus status() default HttpStatus.OK;

    public boolean success() default true;
}
