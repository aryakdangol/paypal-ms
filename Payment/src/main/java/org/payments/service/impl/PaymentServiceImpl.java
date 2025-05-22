package org.payments.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.clients.PaypalWebClient;
import org.payments.dto.CreateOrderDTO;
import org.payments.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service

public class PaymentServiceImpl implements PaymentService {
    @Autowired
   ObjectMapper objectMapper;

    @Autowired
    PaypalWebClient paypalWebClient;

    @Value("${ngrok.url}")
    String hostUrl;
    @Override
    public void createOrder() {

        CreateOrderDTO createOrderDTO = CreateOrderDTO.builder()

                .intent("CAPTURE")
                .paymentSource(CreateOrderDTO.PaymentSource.builder().paypal(CreateOrderDTO.Paypal.builder().experienceContext(CreateOrderDTO.ExperienceContext.builder().cancelUrl(hostUrl  + "/cancel").returnUrl(hostUrl + "/success").build()).build()).build())
                .purchaseUnits(List.of(CreateOrderDTO.PurchaseUnit.builder().amount(CreateOrderDTO.Amount.builder().currencyCode("USD").value("1000").build()).build()))
                .build();


        try {
            log.info("Create order request: {}", objectMapper.writeValueAsString(createOrderDTO));
            Map<String, Object> response = paypalWebClient.createOrder(createOrderDTO);
            log.info("Response from create order: {}", objectMapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void completeOrderSuccess() {

    }

    @Override
    public void completeOrderFailed() {

    }

    @Override
    public void notification() {

    }
}
