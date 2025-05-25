package org.payments.gateway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.payments.gateway.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.payments.gateway.dto.UserDTO;
import org.payments.gateway.exception.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class.getName());
    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("Logging in User..");

        try{
            UserDTO loginCreds = new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);


            Authentication authentication = new UsernamePasswordAuthenticationToken(loginCreds.getUsername(), loginCreds.getPassword());
            return authenticationManager.authenticate(authentication);
        }
        catch (AuthenticationException e){
            log.info("Error occurred  while logging in user...{}", e.getMessage());
            throw new AuthException("Error occurred while logging in User", 500);
        }
        catch (IOException e){
            log.error("Error while parsing login details: {}", e.getMessage());
            throw new AuthException("Error parsing User Login Details", 400);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

        try {
            log.info("User authenticated successfully");


            String token = JwtUtils.generateToken(authResult.getName());

            response.setStatus(200);
            response.setContentType("application/json");
            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            response.getWriter().write(new ObjectMapper().writeValueAsString(data));
            response.getWriter().flush();

        } catch (Exception e) {
            log.error("Error while generating JWT Token: {}", e.getMessage());
            Map<String, String> data = new HashMap<>();
            data.put("error", "Error while generating JWT Token");
            response.setStatus(500);
            response.getWriter().write(new ObjectMapper().writeValueAsString(data));
            response.getWriter().flush();
        }
    }
}
