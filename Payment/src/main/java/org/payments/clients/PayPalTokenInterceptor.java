package org.payments.clients;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.util.Objects;

@Slf4j
@Configuration
@AllArgsConstructor
public class PayPalTokenInterceptor implements RequestInterceptor {

    private final OAuth2AuthorizedClientManager clientManager;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("paypal")
                .principal("paypal-client")
                .build();
        OAuth2AuthorizedClient client = clientManager.authorize(authorizeRequest);
        try{
            String accessToken = Objects.requireNonNull(client).getAccessToken().getTokenValue();
            requestTemplate.header("Authorization", "Bearer " + accessToken);

        }
        catch (Exception e){
            log.info("Error occurred fetching Paypal Access Token: {}", e.getMessage());
            throw new RuntimeException("Error occurred fetching Paypal Access Token");
        }
    }
}
