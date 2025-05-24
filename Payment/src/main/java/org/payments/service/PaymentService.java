package org.payments.service;

import org.payments.dto.CreateOrderRequestDTO;
import org.payments.dto.CreateOrderResponseDTO;

import java.util.List;

public interface PaymentService {

    public List<CreateOrderResponseDTO> findAllOrders(Long userId);

    public CreateOrderResponseDTO getOrder(String orderId, Long UserId);

    public CreateOrderResponseDTO createOrder(Long userId, CreateOrderRequestDTO req);

    public void completeOrderSuccess();

    public void completeOrderFailed();

    public void notification();

}
