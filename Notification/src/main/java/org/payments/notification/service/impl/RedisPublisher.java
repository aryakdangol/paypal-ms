package org.payments.notification.service.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.notification.service.PublisherService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RedisPublisher implements PublisherService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void publishMessage(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
        log.info("Publish event for for channel: {} and message: {}", channel, message);
    }
}
