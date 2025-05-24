package org.payments.exceptions;

import com.payments.common.dtos.ErrorDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class PaymentsMSExceptionHandler {

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ErrorDTO> handlePaymentException(TransactionException ex){
        ErrorDTO error = ErrorDTO.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .status("error")
                .build();
        return new ResponseEntity<>(error, HttpStatusCode.valueOf(ex.getStatus()));
    }
}
