package com.example.basicauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException, ServletException {

        ErrorResponse failure = ErrorResponse.of(HttpStatus.UNAUTHORIZED, "Authentication Unauthorized");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String jsonErrorResponse = objectMapper.writeValueAsString(failure);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(jsonErrorResponse);
        response.getWriter().flush();
    }
}
