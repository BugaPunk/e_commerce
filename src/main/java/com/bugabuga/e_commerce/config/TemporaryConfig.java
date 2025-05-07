package com.bugabuga.e_commerce.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * Configuración temporal para deshabilitar ciertos componentes durante el desarrollo.
 * Esta configuración excluye controladores y servicios que podrían estar causando problemas.
 */
@Configuration
@ConditionalOnProperty(name = "app.config.temporary", havingValue = "true", matchIfMissing = false)
@ComponentScan(
    basePackages = "com.bugabuga.e_commerce",
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.REGEX,
            pattern = {
                "com.bugabuga.e_commerce.controller.AdministracionTiendasController",
                "com.bugabuga.e_commerce.service.TiendaService"
            }
        )
    }
)
public class TemporaryConfig {
    // Esta clase no necesita métodos, solo sirve para configuración
}
