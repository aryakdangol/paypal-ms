package org.payments.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.dto.PaypalAccessTokenDTO;
import org.payments.service.PaypalAuthService;
import org.payments.service.impl.PaymentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    @GetMapping("/createOrder")
    public ResponseEntity<String> checkPayment(){
        log.info("hit");
        paymentService.createOrder();
        return new ResponseEntity<>("hi", HttpStatus.OK);
    }
}
