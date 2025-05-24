package org.payments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderResponseDTO {

    String orderId;
    String username;
    String orderStatus;
    String viewOrderLink;
    String approveLink;
    LocalDateTime orderCreatedDate;

}

