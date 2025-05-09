package com.bugabuga.e_commerce.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para los endpoints de Actuator
 */
@Configuration
public class ActuatorSecurityConfig {

    /**
     * Configura la seguridad para los endpoints de Actuator
     * Esta configuración tiene mayor prioridad que la configuración general de seguridad
     */
    @Bean
    @Order(1)
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(EndpointRequest.toAnyEndpoint())
            .authorizeHttpRequests(authorize -> authorize
                // Endpoints públicos
                .requestMatchers(EndpointRequest.to("health", "info")).permitAll()
                // Endpoints protegidos
                .anyRequest().hasRole("ADMIN")
            )
            .httpBasic();
        
        return http.build();
    }
}
