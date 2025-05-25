package org.payments.notification.service;

public interface NotificationService {

    public void handleOrderSuccess(String orderId);

    public void handleOrderFailed(String orderId);

    public void handleNotification(String orderId);

}
