package org.payments.gateway.security;

import com.payments.utils.Constants;
import com.payments.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.gateway.configs.CustomHttpServletRequestWrapper;
import org.payments.gateway.entity.User;
import org.payments.gateway.service.AuthService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

@Component
@Slf4j
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if(token == null || !token.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        token = token.replace("Bearer ", "");

        String username = JwtUtils.getUser(token);

        User user = authService.findByUsername(username);


        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomHttpServletRequestWrapper modifiedReq = new CustomHttpServletRequestWrapper(request);
        modifiedReq.addHeader(Constants.USERNAME_HEADER, user.getUserName());
        filterChain.doFilter(modifiedReq, response);

    }
}
