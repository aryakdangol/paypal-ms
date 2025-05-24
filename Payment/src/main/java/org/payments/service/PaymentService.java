package org.payments.service;

import org.payments.dto.CreateOrderRequestDTO;
import org.payments.dto.CreateOrderResponseDTO;

public interface PaymentService {

    public CreateOrderResponseDTO createOrder(Long userId, CreateOrderRequestDTO req);

    public void completeOrderSuccess();

    public void completeOrderFailed();

    public void notification();

}
