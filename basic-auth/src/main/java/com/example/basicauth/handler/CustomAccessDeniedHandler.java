package com.example.basicauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorResponse failure = ErrorResponse.of(HttpStatus.FORBIDDEN, "Authentication FORBIDDEN");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String jsonErrorResponse = objectMapper.writeValueAsString(failure);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(jsonErrorResponse);
        response.getWriter().flush();
    }
}
