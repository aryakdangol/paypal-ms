package org.payments.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.payments.common.repositories")
@EntityScan(basePackages = "com.payments.common.entities")
@ComponentScan(basePackages = {
        "org.payments.gateway",             // your gateway service
        "com.payments.common"               // to scan shared services or configs
})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}