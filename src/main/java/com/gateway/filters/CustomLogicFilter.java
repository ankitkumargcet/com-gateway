package com.gateway.filters;

import com.gateway.constants.Constant;
import com.gateway.constants.HttpStatusCustom;
import com.gateway.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
@Component
public class CustomLogicFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("[Gateway] Incoming request: {}", exchange.getRequest().getPath());

        // Handle missing cookie (401 Unauthorized)
        if (exchange.getRequest().getCookies().getFirst(Constant.TOKEN_COOKIE_NAME) == null) {
            return handleError(exchange, HttpStatus.UNAUTHORIZED, "Cookies Unavailable");
        }

        // Process response status after the filter chain
        return chain.filter(exchange).then(Mono.defer(() -> {
            HttpStatusCode statusCode = exchange.getResponse().getStatusCode();
            if (statusCode != null) {
                return handleResponseStatus(exchange, statusCode);
            }
            return Mono.empty();
        })).onErrorResume(e -> handleError(exchange, HttpStatus.INTERNAL_SERVER_ERROR,
                getErrorMessage(HttpStatusCustom.INTERNAL_SERVER_ERROR, exchange.getRequest().getPath().toString())));
    }

    // Handle response status
    private Mono<Void> handleResponseStatus(ServerWebExchange exchange, HttpStatusCode statusCode) {
        if (statusCode.is2xxSuccessful()) {
            logSuccessResponse(exchange, statusCode);
        } else if (statusCode.is3xxRedirection()) {
            logRedirectionResponse(exchange, statusCode);
        } else if (statusCode.is4xxClientError() || statusCode.is5xxServerError()) {
            HttpStatus httpStatus = HttpStatus.valueOf(statusCode.value());
            return handleError(exchange, httpStatus, getErrorMessage(httpStatus, exchange.getRequest().getPath().toString()));
        }
        return Mono.empty();
    }

    // Generate dynamic error message
    private String getErrorMessage(HttpStatus status, String path) {
        return status.is4xxClientError() ? "Client Error: " + status.getReasonPhrase() + " - " + path
                : status.is5xxServerError() ? "Server Error: " + status.getReasonPhrase() + " - " + path
                : "Unexpected Error: " + status.getReasonPhrase();
    }

    // Generate dynamic error message
    @SuppressWarnings("all")
    private String getErrorMessage(HttpStatusCustom status, String path) {
        return status.is5xxServerError() ? "Server Error: " + status.getReasonPhrase() + " - " + path
                : "Unexpected Error: " + status.getReasonPhrase();
    }

    // Log success response (2xx)
    private void logSuccessResponse(ServerWebExchange exchange, HttpStatusCode statusCode) {
        log.info("Success Response: {} for request: {}", statusCode, exchange.getRequest().getPath());
    }

    // Log redirection response (3xx)
    private void logRedirectionResponse(ServerWebExchange exchange, HttpStatusCode statusCode) {
        log.info("Redirection Response: {} for request: {}", statusCode, exchange.getRequest().getPath());
    }

    // Utility method to handle error response
    private Mono<Void> handleError(ServerWebExchange exchange, HttpStatus status, String message) {
        log.error("Error Response: {} - {}", status, message);
        String jsonResponse = createErrorResponse(status, message);
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(jsonResponse.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    // Utility method to create error response JSON
    private String createErrorResponse(HttpStatus status, String message) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(status.value())
                .message(message)
                .timestamp(Instant.now())
                .build();
        return String.format("{\"code\": %d, \"message\": \"%s\", \"timestamp\": \"%s\"}",
                errorResponse.getCode(), errorResponse.getMessage(), errorResponse.getTimestamp());
    }
}

