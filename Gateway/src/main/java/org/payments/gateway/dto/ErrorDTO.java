package org.payments.gateway.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDTO {

    private String status;

    private String message;

    private LocalDateTime timestamp;

}
