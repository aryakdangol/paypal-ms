package org.payments.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/fallback")
public class FallbackController {
    @GetMapping("/payment")
    public ResponseEntity<String> paymentFallback() {
        return ResponseEntity.ok("Payment service is temporarily unavailable. Please try again.");
    }

    @GetMapping("/notification")
    public ResponseEntity<String> notificationFallback() {
        return ResponseEntity.ok("Notification service is temporarily unavailable. Please try again.");
    }
}
