package com.gateway.filters;

import com.gateway.constants.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ServerWebExchange;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomLogicFilterTest {

//    @Autowired
//    private WebTestClient webTestClient;
//
//    private CustomLogicFilter customLogicFilter;
//
//    @BeforeEach
//    void setUp() {
//        customLogicFilter = Mockito.mock(CustomLogicFilter.class);
//    }
//
//    @Test
//    void testFilter_withMissingBluetoken_shouldReturnUnauthorized() {
//        // Mock the ServerWebExchange and GatewayFilterChain
//        ServerWebExchange exchange = Mockito.mock(ServerWebExchange.class);
//        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);
//
//        // Simulate missing bluetoken cookie
//        when(exchange.getRequest().getCookies().getFirst(Constant.TOKEN_COOKIE_NAME)).thenReturn(null);
//
//        // Test the filter logic
//        customLogicFilter.filter(exchange, chain)
//                .doOnTerminate(() -> {
//                    // Ensure that the status code is 401 Unauthorized
//                    verify(exchange.getResponse()).setStatusCode(HttpStatus.UNAUTHORIZED);
//                    // Optionally verify response body (to check if it's serialized correctly)
//                })
//                .block();
//    }
//
//    @Test
//    void testFilter_withBluetoken_shouldProceedToNextFilter() {
//        // Mock the ServerWebExchange and GatewayFilterChain
//        ServerWebExchange exchange = Mockito.mock(ServerWebExchange.class);
//        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);
//
//        // Simulate bluetoken cookie present
//        when(exchange.getRequest().getCookies().getFirst(Constant.TOKEN_COOKIE_NAME)).thenReturn("dummy_token");
//
//        // Test the filter logic
//        customLogicFilter.filter(exchange, chain)
//                .doOnTerminate(() -> {
//                    // Verify the chain.filter() was called, meaning the request was forwarded
//                    verify(chain).filter(exchange);
//                })
//                .block();
//    }
}
