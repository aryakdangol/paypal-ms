package org.payments.notification.controller;

import org.payments.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/success")
    public void success(@RequestParam("token") String orderId){
        notificationService.handleOrderSuccess(orderId);
    }

    @GetMapping("/cancel")
    public void failed(@RequestParam("token") String orderId){
        notificationService.handleOrderFailed(orderId);
    }

    @PostMapping("/receive")
    public ResponseEntity<String> handleNotification(@RequestBody String payload){
        if(notificationService.handleNotification(payload)){
            return new ResponseEntity<String>("IPN Processed", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Invalid IPN", HttpStatus.BAD_REQUEST);
        }
    }

}
