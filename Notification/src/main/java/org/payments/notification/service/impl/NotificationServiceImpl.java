package org.payments.notification.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.payments.notification.service.NotificationService;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {


    @Override
    public void handleOrderSuccess(String orderId) {
        log.info("Payment Approved for order id {}", orderId);

    }

    @Override
    public void handleOrderFailed(String orderId) {
        log.info("Payment cancelled for order id: {}", orderId);

    }

    @Override
    public void handleNotification(String orderId) {

    }
}
