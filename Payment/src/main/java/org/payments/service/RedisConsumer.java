package org.payments.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payments.common.dtos.ErrorDTO;
import com.payments.common.dtos.PubSubDTO;
import com.payments.common.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.dto.CreateOrderResponseDTO;
import org.payments.exceptions.TransactionException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@AllArgsConstructor
public class RedisConsumer implements MessageListener {

    private ObjectMapper  objectMapper;
    private PaymentService paymentService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PubSubDTO body;
        String jsonBody;
        try {
             jsonBody = new String(message.getBody(), StandardCharsets.UTF_8);
            body = objectMapper.readValue(jsonBody, PubSubDTO.class);
            log.info("Consumed event: {} for body: {}", body.getEventType(), body.getOrderId());
        } catch (Exception e) {
            log.error("Error consuming event with cause: {}", e.getMessage());
            return;
        }
        try {
            CreateOrderResponseDTO responseDTO = new CreateOrderResponseDTO();
            switch (body.getEventType()) {
                case Constants.TRANSACTION_APPROVED:
                    responseDTO = paymentService.completeOrderSuccess(body.getOrderId());
                    log.info("=======Response for orderId: {}======== \n with event type: {} is: {}", body.getOrderId(), body.getEventType(), objectMapper.writeValueAsString(responseDTO));
                    break;
                case Constants.TRANSACTION_CANCELLED:
                    responseDTO= paymentService.completeOrderFailed(body.getOrderId());
                    log.info("=======Response for orderId: {}======== \n with event type: {} is: {}", body.getOrderId(), body.getEventType(), objectMapper.writeValueAsString(responseDTO));
                    break;
                default:
                    paymentService.notification(body.getOrderId(), body.getEventType());;
            }
        } catch (Exception e) {
            if (e instanceof TransactionException ex) {
                log.error("Error consuming: {} \nin: {}, With cause: {}, Status Code: {}", jsonBody, ex.getMethod(), ex.getMessage(), ex.getStatus());
            }
            else{
                log.error("Unknown error occurred with cause: {}", e.getMessage());
            }
        }

    }

}
