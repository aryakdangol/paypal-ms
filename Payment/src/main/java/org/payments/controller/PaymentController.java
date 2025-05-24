package org.payments.controller;

import com.payments.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.model.Transaction;
import org.payments.service.PaymentService;
import org.payments.service.impl.PaypalPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/createOrder")
    public ResponseEntity<Transaction> checkPayment(@RequestHeader(Constants.USERNAME_HEADER) String username){
        return new ResponseEntity<>(paymentService.createOrder(username), HttpStatus.CREATED);
    }
}
