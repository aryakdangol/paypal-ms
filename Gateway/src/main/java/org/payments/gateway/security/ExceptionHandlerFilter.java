package org.payments.gateway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.payments.gateway.dto.ErrorDTO;
import org.payments.gateway.exception.AuthException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {


    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }

        catch (AuthException e){
            ErrorDTO error = ErrorDTO.builder()
                    .message(e.getMessage()).status("error")
                    .timestamp(LocalDateTime.now())
                    .build();
            response.setStatus(e.getStatus());
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }
}
