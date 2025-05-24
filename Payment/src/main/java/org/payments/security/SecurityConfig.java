package org.payments.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {


        http.headers((headers) -> {
            headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
        });

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authorize ->
                authorize
                      .anyRequest().permitAll());
        return http.build();
    }

}
