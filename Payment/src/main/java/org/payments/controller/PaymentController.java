package org.payments.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @GetMapping("/request")
    public ResponseEntity<String> checkPayment(){
        log.info("hit");
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }
}
