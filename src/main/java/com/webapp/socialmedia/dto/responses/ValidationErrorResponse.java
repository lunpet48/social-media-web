package com.webapp.socialmedia.dto.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ValidationErrorResponse extends ResponseDTO {
    private List<Violation> errors = new ArrayList<>();

    public ValidationErrorResponse(String message) {
        super(null,true,message);
    }
}

