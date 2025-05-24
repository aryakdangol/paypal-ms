package org.payments.controller;

import com.payments.common.entities.Transaction;
import com.payments.common.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.dto.CreateOrderRequestDTO;
import org.payments.dto.CreateOrderResponseDTO;
import org.payments.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/viewOrder/${orderId}")
    public ResponseEntity<CreateOrderResponseDTO> viewOrder(@PathVariable String orderId){

    }

    @PostMapping("/createOrder")
    public ResponseEntity<CreateOrderResponseDTO> createOrder(@RequestHeader(Constants.USERID_HEADER) String userId,
                                                               @RequestBody CreateOrderRequestDTO req){
        return new ResponseEntity<>(paymentService.createOrder(Long.valueOf(userId), req), HttpStatus.CREATED);
    }
}
