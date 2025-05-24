package org.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PaymentServiceMS {
    public static void main(String[] args) {

        SpringApplication.run(PaymentServiceMS.class);
    }
}