package com.bugabuga.e_commerce.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Interceptor para registrar información sobre las solicitudes HTTP
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final String START_TIME_ATTRIBUTE = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Guardar el tiempo de inicio de la solicitud
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
        
        // Registrar información de la solicitud
        if (logger.isDebugEnabled()) {
            logger.debug("Solicitud recibida: {} {}", request.getMethod(), request.getRequestURI());
            logger.debug("Parámetros: {}", getParameters(request));
            logger.debug("Headers: {}", getHeaders(request));
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // No hacemos nada aquí
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Calcular el tiempo de procesamiento
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            
            // Registrar información de la respuesta
            logger.info("{} {} - {} - {} ms", request.getMethod(), request.getRequestURI(), 
                    response.getStatus(), duration);
            
            if (ex != null) {
                logger.error("Error procesando solicitud: {}", ex.getMessage(), ex);
            }
        }
    }

    /**
     * Obtiene los parámetros de la solicitud como un mapa
     */
    private Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            
            // Ocultar información sensible
            if (name.toLowerCase().contains("password") || name.toLowerCase().contains("token")) {
                value = "******";
            }
            
            parameters.put(name, value);
        }
        
        return parameters;
    }

    /**
     * Obtiene los headers de la solicitud como un mapa
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            
            // Ocultar información sensible
            if (name.toLowerCase().contains("authorization") || name.toLowerCase().contains("cookie")) {
                value = "******";
            }
            
            headers.put(name, value);
        }
        
        return headers;
    }
}
