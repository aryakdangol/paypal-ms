package org.payments.clients;

import org.payments.config.PaypalApiConfig;
import org.payments.dto.paypal.PaypalCreateOrderDTO;
import org.payments.dto.paypal.PaypalCreateOrderResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "paypalClient", url = "${paypal.baseurl}", configuration = PaypalApiConfig.class)
public interface PaypalWebClient {

    @PostMapping("/v2/checkout/orders")
    PaypalCreateOrderResponseDTO createOrder(@RequestBody PaypalCreateOrderDTO paypalCreateOrderDTO);

    @GetMapping("v2/checkout/orders/{orderId}")
    PaypalCreateOrderResponseDTO fetchOrder(@PathVariable String orderId);


}
