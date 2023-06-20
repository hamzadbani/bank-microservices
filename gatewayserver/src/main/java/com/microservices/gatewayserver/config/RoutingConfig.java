package com.microservices.gatewayserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class RoutingConfig {

    @Autowired
    private TokenRelayGatewayFilterFactory filterFactory;

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/microservices/accounts/**")
                        .filters(f -> f.filters(filterFactory.apply())
                                .rewritePath("/microservices/accounts/(?<segment>.*)","/${segment}")
                                .removeRequestHeader("Cookie"))
                        .uri("lb://ACCOUNTS")).
                route(p -> p
                        .path("/microservices/loans/**")
                        .filters(f -> f.filters(filterFactory.apply())
                                .rewritePath("/microservices/loans/(?<segment>.*)","/${segment}")
                                .removeRequestHeader("Cookie"))
                        .uri("lb://LOANS")).
                route(p -> p
                        .path("/microservices/cards/**")
                        .filters(f -> f.filters(filterFactory.apply())
                                .rewritePath("/microservices/cards/(?<segment>.*)","/${segment}")
                                .removeRequestHeader("Cookie"))
                        .uri("lb://CARDS")).build();
    }

}
