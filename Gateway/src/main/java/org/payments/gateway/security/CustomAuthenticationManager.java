package org.payments.gateway.security;

import lombok.AllArgsConstructor;
import com.payments.common.entities.User;
import org.payments.gateway.exception.AuthException;
import org.payments.gateway.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final AuthService authService;
    private  final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = authService.findByUsername(authentication.getName());
        if(bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())){

            return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials());

        } else{
            throw new AuthException("The password for the username does not match", 400);
        }
    }
}
