package org.payments.notification.service;

public interface PublisherService {

    public void publishMessage(String channel, String message);
}
