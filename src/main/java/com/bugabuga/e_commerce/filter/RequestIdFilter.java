package com.bugabuga.e_commerce.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtro para añadir un ID único a cada solicitud HTTP
 * Esto facilita el seguimiento de solicitudes en los logs
 */
@Component
public class RequestIdFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_HEADER_NAME = "X-Request-ID";
    private static final String REQUEST_ID_MDC_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            // Obtener el ID de solicitud del header o generar uno nuevo
            String requestId = request.getHeader(REQUEST_ID_HEADER_NAME);
            if (requestId == null || requestId.isEmpty()) {
                requestId = generateRequestId();
            }
            
            // Añadir el ID de solicitud al MDC para que aparezca en los logs
            MDC.put(REQUEST_ID_MDC_KEY, requestId);
            
            // Añadir el ID de solicitud a la respuesta
            response.setHeader(REQUEST_ID_HEADER_NAME, requestId);
            
            // Continuar con la cadena de filtros
            filterChain.doFilter(request, response);
        } finally {
            // Limpiar el MDC para evitar fugas de memoria
            MDC.remove(REQUEST_ID_MDC_KEY);
        }
    }

    /**
     * Genera un ID de solicitud único
     */
    private String generateRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
