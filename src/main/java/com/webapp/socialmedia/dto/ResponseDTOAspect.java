package com.webapp.socialmedia.dto;

import com.webapp.socialmedia.dto.responses.ResponseDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResponseDTOAspect {

    @Around("@annotation(wrappingResponse))")
    public Object wrapWithResponseDTO(ProceedingJoinPoint joinPoint, final WrappingResponse wrappingResponse) throws Throwable {
        Object retVal = joinPoint.proceed();
        ResponseDTO<Object> responseDTO;
        if (!wrappingResponse.success())
            responseDTO = ResponseDTO.builder().message((String) retVal).data(null).isSuccessful(false).build();
        else {
            responseDTO = ResponseDTO.builder().message("").isSuccessful(wrappingResponse.success()).data(retVal).build();
        }
        return ResponseEntity.status(wrappingResponse.status()).body(responseDTO);
    }
}
