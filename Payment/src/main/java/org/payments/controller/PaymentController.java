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

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/viewOrder/{orderId}")
    public ResponseEntity<CreateOrderResponseDTO> viewOrder(@RequestHeader(Constants.USERID_HEADER) String userId, @PathVariable String orderId){
        return new ResponseEntity<CreateOrderResponseDTO>(paymentService.getOrder(orderId, Long.valueOf(userId)), HttpStatus.OK);
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<List<CreateOrderResponseDTO>> getAllOrders(@RequestHeader(Constants.USERID_HEADER) String userId){
       return new ResponseEntity<>(paymentService.findAllOrders(Long.valueOf(userId)), HttpStatus.OK);

    }

    @PostMapping("/createOrder")
    public ResponseEntity<CreateOrderResponseDTO> createOrder(@RequestHeader(Constants.USERID_HEADER) String userId,
                                                               @RequestBody CreateOrderRequestDTO req){
        return new ResponseEntity<>(paymentService.createOrder(Long.valueOf(userId), req), HttpStatus.CREATED);
    }

//    @PostMapping("/captureOrder/{orderId}")
//    public ResponseEntity<CreateOrderResponseDTO> createOrder(@RequestHeader(Constants.USERID_HEADER) String userId,
//                                                              @PathVariable String orderId){
//        return new ResponseEntity<>(paymentService.completeOrderSuccess(orderId, Long.valueOf(userId)), HttpStatus.OK);
//    }

    @GetMapping("/orders/success")
    public void success(@RequestParam("token") String orderId){
        paymentService.completeOrderSuccess(orderId);
    }

    @GetMapping("/orders/cancel")
    public void failed(@RequestParam("token") String orderId){
        paymentService.completeOrderFailed(orderId);
    }
}
