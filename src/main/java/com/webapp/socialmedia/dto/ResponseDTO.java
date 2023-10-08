package com.webapp.socialmedia.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ResponseDTO<T>{
    private T data;
    private boolean isSuccessful;
    private String message;
}
