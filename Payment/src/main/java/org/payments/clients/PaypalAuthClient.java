package org.payments.clients;

import org.payments.dto.PaypalAccessTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="paypalAuthClient", url = "${paypal.baseurl}")
public interface PaypalAuthClient {


    @PostMapping(value = "v1/oauth2/token", consumes = "application/x-www-form-urlencoded")
    PaypalAccessTokenDTO getAccessToken(@RequestHeader("Authorization") String auth, @RequestParam("grant_type") String grantType);

}
