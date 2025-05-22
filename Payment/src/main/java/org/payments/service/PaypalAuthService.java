package org.payments.service;
import org.payments.clients.PaypalAuthClient;
import org.payments.dto.PaypalAccessTokenDTO;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class PaypalAuthService {

    private final PaypalAuthClient paypalAuthClient;

    private String clientId;

    private String clientSecret;


    public PaypalAuthService(PaypalAuthClient paypalAuthClient) {
        this.paypalAuthClient = paypalAuthClient;
        this.clientId = System.getenv("PAYPAL_CLIENT_ID");
        this.clientSecret = System.getenv("PAYPAL_CLIENT_SECRET");
    }

    public PaypalAccessTokenDTO fetchAccessToken(){
        String auth = "Basic " +
                Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        PaypalAccessTokenDTO response = paypalAuthClient.getAccessToken(auth, "client_credentials");

        return response;

    }
}
