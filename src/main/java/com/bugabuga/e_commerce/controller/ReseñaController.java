package com.bugabuga.e_commerce.controller;

import com.bugabuga.e_commerce.model.entity.Reseña;
import com.bugabuga.e_commerce.service.ReseñaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reseñas")
@Validated
public class ReseñaController {

    private final ReseñaService reseñaService;
    private static final Logger logger = LoggerFactory.getLogger(ReseñaController.class);

    @Autowired
    public ReseñaController(ReseñaService reseñaService) {
        this.reseñaService = reseñaService;
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Map<String, Object>> obtenerReseñasPorProducto(
            @PathVariable Long productoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaCreacion") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        long inicio = System.currentTimeMillis();
        logger.info("[RESEÑA] Inicio obtenerReseñasPorProducto: {}", inicio);
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<Reseña> reseñas = reseñaService.obtenerReseñasPorProducto(productoId, pageable);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("reseñas", reseñas.getContent());
        respuesta.put("currentPage", reseñas.getNumber());
        respuesta.put("totalItems", reseñas.getTotalElements());
        respuesta.put("totalPages", reseñas.getTotalPages());
        
        Double calificacionPromedio = reseñaService.calcularCalificacionPromedio(productoId);
        respuesta.put("calificacionPromedio", calificacionPromedio);
        
        long fin = System.currentTimeMillis();
        logger.info("[RESEÑA] Fin obtenerReseñasPorProducto: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerReseñasPorUsuario(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        long inicio = System.currentTimeMillis();
        logger.info("[RESEÑA] Inicio obtenerReseñasPorUsuario: {}", inicio);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaCreacion"));
        
        Page<Reseña> reseñas = reseñaService.obtenerReseñasPorUsuario(usuarioId, pageable);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("reseñas", reseñas.getContent());
        respuesta.put("currentPage", reseñas.getNumber());
        respuesta.put("totalItems", reseñas.getTotalElements());
        respuesta.put("totalPages", reseñas.getTotalPages());
        
        long fin = System.currentTimeMillis();
        logger.info("[RESEÑA] Fin obtenerReseñasPorUsuario: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Reseña> crearReseña(@Valid @RequestBody Reseña reseña) {
        long inicio = System.currentTimeMillis();
        logger.info("[RESEÑA] Inicio crearReseña: {}", inicio);
        
        Reseña nuevaReseña = reseñaService.crearReseña(reseña);
        
        long fin = System.currentTimeMillis();
        logger.info("[RESEÑA] Fin crearReseña: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReseña);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Reseña> actualizarReseña(
            @PathVariable Long id,
            @Valid @RequestBody Reseña reseña) {
        long inicio = System.currentTimeMillis();
        logger.info("[RESEÑA] Inicio actualizarReseña: {}", inicio);
        
        Reseña reseñaActualizada = reseñaService.actualizarReseña(id, reseña);
        
        long fin = System.currentTimeMillis();
        logger.info("[RESEÑA] Fin actualizarReseña: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(reseñaActualizada);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarReseña(@PathVariable Long id) {
        long inicio = System.currentTimeMillis();
        logger.info("[RESEÑA] Inicio eliminarReseña: {}", inicio);
        
        reseñaService.eliminarReseña(id);
        
        long fin = System.currentTimeMillis();
        logger.info("[RESEÑA] Fin eliminarReseña: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.noContent().build();
    }
}
