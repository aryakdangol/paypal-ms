package org.payments.clients;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import org.payments.service.PaypalAuthService;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class PayPalTokenInterceptor implements RequestInterceptor {

    private final PaypalAuthService paypalAuthService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String accessToken = paypalAuthService.fetchAccessToken().getAccessToken();
        requestTemplate.header("Authorization", "Bearer " + accessToken);
    }
}
