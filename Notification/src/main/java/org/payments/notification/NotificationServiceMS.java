package org.payments.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@ComponentScan(basePackages = {
        "org.payments.notification",             // your gateway service
        "com.payments.common"               // to scan shared services or configs
})
public class NotificationServiceMS {
    public  static void main(String... args){
        SpringApplication.run(NotificationServiceMS.class, args);
    }
}