package com.webapp.socialmedia.dto.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO{
    private Object data;
    private boolean error;
    private String message;

    public ResponseDTO success(Object data){
        return new ResponseDTO(data, false, "");
    }
}
