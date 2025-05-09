package com.bugabuga.e_commerce.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Configuración de caché para la aplicación
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Nombres de las cachés utilizadas en la aplicación
     */
    public static final String CACHE_PRODUCTOS = "productos";
    public static final String CACHE_PRODUCTO = "producto";
    public static final String CACHE_CATEGORIAS = "categorias";
    public static final String CACHE_TIENDAS = "tiendas";
    public static final String CACHE_PRODUCTOS_POR_CATEGORIA = "productosPorCategoria";
    public static final String CACHE_PRODUCTOS_POR_TIENDA = "productosPorTienda";
    public static final String CACHE_PRODUCTOS_RECIENTES = "productosRecientes";

    /**
     * Configura el gestor de caché con Caffeine
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        // Configurar las cachés que se utilizarán
        cacheManager.setCacheNames(Arrays.asList(
                CACHE_PRODUCTOS,
                CACHE_PRODUCTO,
                CACHE_CATEGORIAS,
                CACHE_TIENDAS,
                CACHE_PRODUCTOS_POR_CATEGORIA,
                CACHE_PRODUCTOS_POR_TIENDA,
                CACHE_PRODUCTOS_RECIENTES
        ));
        
        // Configuración por defecto para todas las cachés
        cacheManager.setCaffeine(caffeineCacheBuilder());
        
        return cacheManager;
    }

    /**
     * Configura el builder de Caffeine con los parámetros de caché
     */
    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)     // Capacidad inicial
                .maximumSize(1000)        // Tamaño máximo
                .expireAfterWrite(30, TimeUnit.MINUTES)  // Tiempo de expiración
                .recordStats();           // Registrar estadísticas
    }
}
