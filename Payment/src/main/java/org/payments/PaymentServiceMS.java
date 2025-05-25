package org.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableFeignClients
@EnableRetry
@EnableJpaRepositories(basePackages = "com.payments.common.repositories")
@EntityScan(basePackages = "com.payments.common.entities")
@ComponentScan(basePackages = {
        "org.payments",
        "com.payments.common"
})
public class PaymentServiceMS {
    public static void main(String[] args) {

        SpringApplication.run(PaymentServiceMS.class);
    }
}