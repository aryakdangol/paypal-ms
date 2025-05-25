package org.payments.notification.service;

import com.payments.common.dtos.PubSubDTO;

public interface PublisherService {

    public void publishMessage(PubSubDTO message);
}
