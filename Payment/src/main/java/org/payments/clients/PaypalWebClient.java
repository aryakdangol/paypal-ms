package org.payments.clients;

import org.payments.config.PaypalApiConfig;
import org.payments.dto.CreateOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "paypalClient", url = "${paypal.baseurl}", configuration = PaypalApiConfig.class)
public interface PaypalWebClient {

    @PostMapping("/v2/checkout/orders")
    Map<String, Object> createOrder(@RequestBody CreateOrderDTO createOrderDTO);


}
