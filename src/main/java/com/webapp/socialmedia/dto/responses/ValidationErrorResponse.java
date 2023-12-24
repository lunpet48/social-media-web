package com.webapp.socialmedia.dto.responses;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationErrorResponse extends ResponseDTO {
    private List<Violation> errors = new ArrayList<>();

    public ValidationErrorResponse(String message) {
        super(null,true,message);
    }
}

