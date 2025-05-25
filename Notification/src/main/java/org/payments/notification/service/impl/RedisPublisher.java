package org.payments.notification.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payments.common.dtos.PubSubDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.notification.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisPublisher implements PublisherService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Value("${redis.channel.name}")
    String channel;

    @Autowired
    ObjectMapper objectMapper;


    @Override
    public void publishMessage(PubSubDTO message) {
        try{
            redisTemplate.convertAndSend(channel, objectMapper.writeValueAsString(message));
            log.info("Published event: {} for order id: {}", message.getEventType(), message.getOrderId());
        }
        catch (Exception ex){
            log.error("Failed to publish event: {} for order id: {}", message.getEventType(), message.getOrderId());
            log.error("Publish event cause: {}", ex.getMessage());
        }
    }
}
