package com.webapp.socialmedia.dto.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO<T>{
    private T data;
    private boolean isSuccessful;
    private String message;
}
