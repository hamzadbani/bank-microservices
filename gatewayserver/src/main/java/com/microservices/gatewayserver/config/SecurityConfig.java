package com.microservices.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges.pathMatchers("/microservices/accounts/**").authenticated()
                        .pathMatchers("/microservices/cards/**").authenticated()
                        .pathMatchers("/microservices/loans/**").permitAll())
                .oauth2Login(Customizer.withDefaults());
        http.csrf((csrf) -> csrf.disable());
        return http.build();
    }
}
