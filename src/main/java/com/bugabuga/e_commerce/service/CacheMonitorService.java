package com.bugabuga.e_commerce.service;

import com.bugabuga.e_commerce.config.CacheConfig;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Servicio para monitorear el rendimiento de la caché
 */
@Service
public class CacheMonitorService {

    private static final Logger logger = LoggerFactory.getLogger(CacheMonitorService.class);
    private final CacheManager cacheManager;

    public CacheMonitorService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Registra estadísticas de caché cada 15 minutos
     */
    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.MINUTES)
    public void logCacheStatistics() {
        logger.info("=== Estadísticas de Caché ===");
        
        // Obtener estadísticas para cada caché
        Map<String, CacheStats> statsMap = getCacheStatistics();
        
        // Registrar estadísticas
        statsMap.forEach((cacheName, stats) -> {
            logger.info("Caché: {}", cacheName);
            logger.info("  Hits: {}", stats.hitCount());
            logger.info("  Misses: {}", stats.missCount());
            logger.info("  Hit Rate: {}%", String.format("%.2f", stats.hitRate() * 100));
            logger.info("  Evictions: {}", stats.evictionCount());
            logger.info("  Average Load Penalty: {} ms", String.format("%.2f", stats.averageLoadPenalty() / 1_000_000.0));
        });
        
        logger.info("============================");
    }

    /**
     * Obtiene estadísticas de todas las cachés
     */
    public Map<String, CacheStats> getCacheStatistics() {
        Map<String, CacheStats> statsMap = new HashMap<>();
        
        // Obtener estadísticas para cada caché
        getCacheStatistics(CacheConfig.CACHE_PRODUCTOS, statsMap);
        getCacheStatistics(CacheConfig.CACHE_PRODUCTO, statsMap);
        getCacheStatistics(CacheConfig.CACHE_CATEGORIAS, statsMap);
        getCacheStatistics(CacheConfig.CACHE_TIENDAS, statsMap);
        getCacheStatistics(CacheConfig.CACHE_PRODUCTOS_POR_CATEGORIA, statsMap);
        getCacheStatistics(CacheConfig.CACHE_PRODUCTOS_POR_TIENDA, statsMap);
        getCacheStatistics(CacheConfig.CACHE_PRODUCTOS_RECIENTES, statsMap);
        
        return statsMap;
    }

    /**
     * Obtiene estadísticas para una caché específica
     */
    private void getCacheStatistics(String cacheName, Map<String, CacheStats> statsMap) {
        CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(cacheName);
        if (caffeineCache != null) {
            Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
            statsMap.put(cacheName, nativeCache.stats());
        }
    }

    /**
     * Limpia todas las cachés
     */
    public void clearAllCaches() {
        logger.info("Limpiando todas las cachés");
        
        cacheManager.getCacheNames().forEach(cacheName -> {
            logger.debug("Limpiando caché: {}", cacheName);
            cacheManager.getCache(cacheName).clear();
        });
        
        logger.info("Todas las cachés han sido limpiadas");
    }
}
