package org.payments.gateway.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {


    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final AuthenticationFilter authenticationFilter;
    private final JwtFilter jwtFilter;


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{

        authenticationFilter.setFilterProcessesUrl("/auth/login");


        http.headers((headers) -> {
            headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
        });

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authorize ->

            authorize.requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/api/payments/viewOrder/**").authenticated()
                    .requestMatchers("/api/payments/getAllOrders").authenticated()
                    .requestMatchers("/api/payments/createOrder").authenticated()
                    .requestMatchers("/api/payments/orders/**").permitAll()
                    .requestMatchers("/api/payments/captureOrder/**").permitAll()
                    .requestMatchers("/api/notification/**").permitAll()
                    .anyRequest().authenticated()
        )
                .addFilterBefore(exceptionHandlerFilter, AuthenticationFilter.class)
                .addFilter(authenticationFilter)
                .addFilterBefore(jwtFilter, AuthenticationFilter.class);

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}
