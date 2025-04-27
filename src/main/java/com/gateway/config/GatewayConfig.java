package com.gateway.config;

import com.gateway.constants.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${com.api.url}")
    private String comApiUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(Constant.COM_API_ROUTE_ID, r -> r.path(Constant.COM_API_ROUTE_PATHS)
                        .uri(comApiUrl)) // API Application URL
                .build();
    }
}
