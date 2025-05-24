package org.payments.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * This class is for authentication with paypal api
 * We need to get access token from: https://api-m.sandbox.paypal.com/v1/oauth2/token to be able to interact with the payments
 * This class sends the request to the api and caches the access_token in memory
 * This is then register under PayPalTokenInterceptor class
 */
@Configuration
public class AccessTokenConfig {

    @Value("${paypal.authurl}")
    private String paypalUrl;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("paypal")
                .tokenUri(paypalUrl)
                .clientId(System.getenv("PAYPAL_CLIENT_ID"))
                .clientSecret(System.getenv("PAYPAL_CLIENT_SECRET"))
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();

        return new InMemoryClientRegistrationRepository(clientRegistration);
    }


    @Bean
    public OAuth2AuthorizedClientManager clientManager(ClientRegistrationRepository clientRegistrationRepository){

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository));

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;

    }

}
