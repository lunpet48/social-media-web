package com.webapp.socialmedia.dto;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ResponseDTOAspect {

    @Around("@annotation(wrappingResponse))")
    public Object wrapWithResponseDTO(ProceedingJoinPoint joinPoint, final WrappingResponse wrappingResponse) throws Throwable {
        Object retVal = joinPoint.proceed();
//        ResponseEntity<Object> o = (ResponseEntity<Object>) retVal;
//        return new ResponseDTO(o.getStatusCode(), o.getBody(), Boolean.TRUE, "");
        ResponseDTO<Object> responseDTO = ResponseDTO.builder().message("").isSuccessful(true).data(retVal).build();
        return ResponseEntity.status(wrappingResponse.status()).body(responseDTO);
    }
}
