package com.bugabuga.e_commerce.exception;

import com.bugabuga.e_commerce.model.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para estandarizar las respuestas de error de la API
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de recurso no encontrado
     * @return ResponseEntity con estado 404 NOT_FOUND
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiResponse<?> response = ApiResponse.error(ex.getMessage(), null, path);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Maneja excepciones de validación de argumentos de método
     * @return ResponseEntity con estado 400 BAD_REQUEST
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiResponse<?> response = ApiResponse.error("Error de validación", errors, path);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja excepciones de violación de restricciones
     * @return ResponseEntity con estado 400 BAD_REQUEST
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiResponse<?> response = ApiResponse.error("Error de validación", errors, path);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja excepciones de tipo de argumento incorrecto
     * @return ResponseEntity con estado 400 BAD_REQUEST
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        String message = String.format("El parámetro '%s' debería ser de tipo '%s'",
                ex.getName(), ex.getRequiredType().getSimpleName());
        ApiResponse<?> response = ApiResponse.error(message, null, path);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja excepciones de parámetro de solicitud faltante
     * @return ResponseEntity con estado 400 BAD_REQUEST
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        String message = String.format("El parámetro '%s' es obligatorio", ex.getParameterName());
        ApiResponse<?> response = ApiResponse.error(message, null, path);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja excepciones de mensaje HTTP no legible
     * @return ResponseEntity con estado 400 BAD_REQUEST
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiResponse<?> response = ApiResponse.error("El cuerpo de la solicitud no es válido", null, path);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja excepciones de integridad de datos
     * @return ResponseEntity con estado 409 CONFLICT
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiResponse<?> response = ApiResponse.error("Error de integridad de datos: " + ex.getMostSpecificCause().getMessage(), null, path);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Maneja excepciones de argumento ilegal
     * @return ResponseEntity con estado 400 BAD_REQUEST
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiResponse<?> response = ApiResponse.error(ex.getMessage(), null, path);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja excepciones de autenticación
     * @return ResponseEntity con estado 401 UNAUTHORIZED
     */
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ApiResponse<?>> handleAuthentication(Exception ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiResponse<?> response = ApiResponse.error("Error de autenticación: " + ex.getMessage(), null, path);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Maneja excepciones de acceso denegado
     * @return ResponseEntity con estado 403 FORBIDDEN
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiResponse<?> response = ApiResponse.error("Acceso denegado: " + ex.getMessage(), null, path);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Maneja cualquier otra excepción no capturada
     * @return ResponseEntity con estado 500 INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> globalExceptionHandler(Exception ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiResponse<?> response = ApiResponse.error("Error interno del servidor: " + ex.getMessage(), null, path);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
