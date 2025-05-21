package org.payments.gateway.exception;

import org.payments.gateway.dto.ErrorDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GatewayExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorDTO> handleAuthException(AuthException e){
        ErrorDTO errorDTO = ErrorDTO.builder().
                message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status("error")
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatusCode.valueOf(e.getStatus()));
    }


}
