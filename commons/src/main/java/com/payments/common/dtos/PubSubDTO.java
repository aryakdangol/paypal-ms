package com.payments.common.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PubSubDTO {
    String orderId;
    String eventType;
}
