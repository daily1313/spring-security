package com.example.oauth.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequestLog(request, body);

        ClientHttpResponse response = execution.execute(request, body);

        traceResponseLog(response, request.getURI());
        return response;
    }

    private void traceRequestLog(HttpRequest request, byte[] body) {
        log.debug("[REQUEST] URI: {}, Method: {}, Request Body: {}",
                request.getURI(),
                request.getMethod(),
                new String(body, StandardCharsets.UTF_8));
    }

    private void traceResponseLog(ClientHttpResponse response, URI uri) throws IOException {
        log.debug("[RESPONSE] URI: {}, Status code: {}, Response Body: {}",
                uri,
                response.getStatusCode(),
                StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));
    }
}
