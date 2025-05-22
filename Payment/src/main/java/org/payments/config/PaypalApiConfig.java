package org.payments.config;

import feign.RequestInterceptor;
import lombok.AllArgsConstructor;
import org.payments.clients.PayPalTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class PaypalApiConfig {

    private final PayPalTokenInterceptor payPalTokenInterceptor;


    @Bean
    public RequestInterceptor pauPalTokenrequestInterceptor(){
        return payPalTokenInterceptor;
    }


}
