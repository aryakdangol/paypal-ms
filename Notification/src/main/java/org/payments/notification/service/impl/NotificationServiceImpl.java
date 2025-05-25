package org.payments.notification.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payments.common.dtos.PubSubDTO;
import com.payments.common.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.payments.notification.service.NotificationService;

import org.payments.notification.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    ObjectMapper objectMapper;

    @Value("${ipn.verify.url}")
    String verifyUrl;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PublisherService publisherService;


    @Override
    public void handleOrderSuccess(String orderId) {
        log.info("Payment Approved for order id {}", orderId);
        PubSubDTO message = PubSubDTO.builder().eventType(Constants.TRANSACTION_APPROVED).orderId(orderId).build();
        publisherService.publishMessage(message);
    }

    @Override
    public void handleOrderFailed(String orderId) {
        log.info("Payment cancelled for order id: {}", orderId);
        PubSubDTO message = PubSubDTO.builder().eventType(Constants.TRANSACTION_CANCELLED).orderId(orderId).build();
        publisherService.publishMessage(message);
    }

    @Override
    public boolean handleNotification(String payload) {
        try{
        log.info("IPN Payload: {}", payload);
            String modifiedPayload = "cmd" + "=" + "_notify-validate" +  "&" + payload;

                    HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<String> request = new HttpEntity<>(modifiedPayload, headers);

            log.info("Sending verification to Paypal");
            ResponseEntity<String> verifiedResponse = restTemplate.postForEntity(verifyUrl, request, String.class);

            log.info("GOT Verification Response from Paypal: {}", verifiedResponse.getBody());

            if(Objects.equals(verifiedResponse.getBody(), "VERIFIED")){
                Map<String, String> params = Arrays.stream(payload.split("&"))
                        .map(s -> s.split("="))
                        .collect(Collectors.toMap(
                                a -> URLDecoder.decode(a[0], StandardCharsets.UTF_8),
                                a -> URLDecoder.decode(a.length > 1 ? a[1] : "", StandardCharsets.UTF_8)
                        ));
                log.info("Verified IPN purchase: {}", objectMapper.writeValueAsString(params));
                String orderId = params.get("invoice");
                String orderStatus = params.get("payment_status");
                PubSubDTO pubSubDTO = PubSubDTO.builder().eventType(orderStatus).orderId(orderId).build();
                publisherService.publishMessage(pubSubDTO);
                return true;
            }
            return false;
        }
        catch (Exception e){
            log.error("Error fetching ipn status: {}", e.getMessage());
            return false;
        }

    }
}
