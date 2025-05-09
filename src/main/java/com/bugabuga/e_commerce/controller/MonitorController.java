package com.bugabuga.e_commerce.controller;

import com.bugabuga.e_commerce.model.dto.ApiResponse;
import com.bugabuga.e_commerce.service.CacheMonitorService;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para monitoreo de la aplicación
 * Solo accesible para administradores
 */
@RestController
@RequestMapping("/api/admin/monitor")
@PreAuthorize("hasRole('ADMIN')")
public class MonitorController {

    private final CacheMonitorService cacheMonitorService;

    @Autowired
    public MonitorController(CacheMonitorService cacheMonitorService) {
        this.cacheMonitorService = cacheMonitorService;
    }

    /**
     * Obtiene estadísticas de caché
     */
    @GetMapping("/cache")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCacheStatistics() {
        Map<String, CacheStats> statsMap = cacheMonitorService.getCacheStatistics();
        
        Map<String, Object> result = new HashMap<>();
        statsMap.forEach((cacheName, stats) -> {
            Map<String, Object> cacheStats = new HashMap<>();
            cacheStats.put("hits", stats.hitCount());
            cacheStats.put("misses", stats.missCount());
            cacheStats.put("hitRate", String.format("%.2f%%", stats.hitRate() * 100));
            cacheStats.put("evictions", stats.evictionCount());
            cacheStats.put("averageLoadPenalty", String.format("%.2f ms", stats.averageLoadPenalty() / 1_000_000.0));
            
            result.put(cacheName, cacheStats);
        });
        
        return ResponseEntity.ok(ApiResponse.success("Estadísticas de caché obtenidas con éxito", result));
    }

    /**
     * Limpia todas las cachés
     */
    @PostMapping("/cache/clear")
    public ResponseEntity<ApiResponse<Void>> clearAllCaches() {
        cacheMonitorService.clearAllCaches();
        return ResponseEntity.ok(ApiResponse.success("Todas las cachés han sido limpiadas"));
    }

    /**
     * Obtiene información del sistema
     */
    @GetMapping("/system")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        
        // Información de memoria
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memoryBean.getNonHeapMemoryUsage();
        
        Map<String, Object> memoryInfo = new HashMap<>();
        memoryInfo.put("heapMemoryUsed", formatSize(heapMemoryUsage.getUsed()));
        memoryInfo.put("heapMemoryMax", formatSize(heapMemoryUsage.getMax()));
        memoryInfo.put("heapMemoryUsage", String.format("%.2f%%", 
                (double) heapMemoryUsage.getUsed() / heapMemoryUsage.getMax() * 100));
        memoryInfo.put("nonHeapMemoryUsed", formatSize(nonHeapMemoryUsage.getUsed()));
        
        systemInfo.put("memory", memoryInfo);
        
        // Información de tiempo de ejecución
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        systemInfo.put("uptime", formatUptime(uptime));
        
        // Información de procesadores
        systemInfo.put("processors", Runtime.getRuntime().availableProcessors());
        
        // Información de JVM
        Map<String, String> jvmInfo = new HashMap<>();
        jvmInfo.put("version", System.getProperty("java.version"));
        jvmInfo.put("vendor", System.getProperty("java.vendor"));
        jvmInfo.put("vmName", System.getProperty("java.vm.name"));
        
        systemInfo.put("jvm", jvmInfo);
        
        return ResponseEntity.ok(ApiResponse.success("Información del sistema obtenida con éxito", systemInfo));
    }
    
    /**
     * Formatea el tamaño en bytes a una representación legible
     */
    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    /**
     * Formatea el tiempo de actividad en milisegundos a una representación legible
     */
    private String formatUptime(long uptimeMillis) {
        long seconds = uptimeMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        return String.format("%d días, %d horas, %d minutos, %d segundos",
                days, hours % 24, minutes % 60, seconds % 60);
    }
}
