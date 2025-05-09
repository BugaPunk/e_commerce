package com.bugabuga.e_commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuración CORS para permitir solicitudes desde cualquier origen.
 * Esto es útil durante el desarrollo, pero debe ser más restrictivo en producción.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Permitir solicitudes desde orígenes específicos
        config.setAllowCredentials(true);

        // No se puede usar "*" con allowCredentials=true, usamos allowedOriginPatterns
        config.addAllowedOriginPattern("*"); // En producción, especificar dominios concretos

        config.addAllowedHeader("*");
        config.addExposedHeader("Authorization"); // Exponer el header de Authorization

        // Permitir todos los métodos HTTP comunes
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
