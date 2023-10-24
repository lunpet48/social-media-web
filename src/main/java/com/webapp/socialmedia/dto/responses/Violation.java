package com.webapp.socialmedia.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Violation {
    private final String field;
    private final String defaultMessage;
}
