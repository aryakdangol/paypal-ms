package org.payments.notification.controller;

import org.payments.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/sendResult")
    public void handleNotification(@RequestBody String payload){
        notificationService.handleNotification(payload);
    }

}
