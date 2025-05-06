package com.bugabuga.e_commerce.controller;

import com.bugabuga.e_commerce.model.dto.ProductoDTO;
import com.bugabuga.e_commerce.model.entity.Tienda;
import com.bugabuga.e_commerce.service.TiendaService;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/tiendas")
@Validated
public class AdministracionTiendasController {

    private final TiendaService tiendaService;
    private static final Logger logger = LoggerFactory.getLogger(AdministracionTiendasController.class);

    @Autowired
    public AdministracionTiendasController(TiendaService tiendaService) {
        this.tiendaService = tiendaService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodasLasTiendas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sort) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Inicio obtenerTodasLasTiendas: {}", inicio);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Tienda> tiendas = tiendaService.obtenerTodasLasTiendas(pageable);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("tiendas", tiendas.getContent());
        respuesta.put("currentPage", tiendas.getNumber());
        respuesta.put("totalItems", tiendas.getTotalElements());
        respuesta.put("totalPages", tiendas.getTotalPages());
        
        long fin = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Fin obtenerTodasLasTiendas: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tienda> obtenerTiendaPorId(@PathVariable Long id) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Inicio obtenerTiendaPorId: {}", inicio);
        
        Tienda tienda = tiendaService.obtenerTiendaPorId(id);
        
        long fin = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Fin obtenerTiendaPorId: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(tienda);
    }

    @GetMapping("/propietario/{propietarioId}")
    public ResponseEntity<List<Tienda>> obtenerTiendasPorPropietario(@PathVariable Long propietarioId) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Inicio obtenerTiendasPorPropietario: {}", inicio);
        
        List<Tienda> tiendas = tiendaService.obtenerTiendasPorPropietario(propietarioId);
        
        long fin = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Fin obtenerTiendasPorPropietario: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(tiendas);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Tienda> crearTienda(@Valid @RequestBody Tienda tienda) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Inicio crearTienda: {}", inicio);
        
        Tienda nuevaTienda = tiendaService.crearTienda(tienda);
        
        long fin = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Fin crearTienda: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTienda);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Tienda> actualizarTienda(
            @PathVariable Long id,
            @Valid @RequestBody Tienda tienda) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Inicio actualizarTienda: {}", inicio);
        
        Tienda tiendaActualizada = tiendaService.actualizarTienda(id, tienda);
        
        long fin = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Fin actualizarTienda: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(tiendaActualizada);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> desactivarTienda(@PathVariable Long id) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Inicio desactivarTienda: {}", inicio);
        
        tiendaService.desactivarTienda(id);
        
        long fin = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Fin desactivarTienda: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tiendaId}/productos")
    @Transactional
    public ResponseEntity<ProductoDTO> agregarProductoATienda(
            @PathVariable Long tiendaId,
            @Valid @RequestBody ProductoDTO productoDTO) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Inicio agregarProductoATienda: {}", inicio);
        
        ProductoDTO nuevoProducto = tiendaService.agregarProductoATienda(tiendaId, productoDTO);
        
        long fin = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Fin agregarProductoATienda: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @GetMapping("/{tiendaId}/productos")
    public ResponseEntity<Map<String, Object>> obtenerProductosDeTienda(
            @PathVariable Long tiendaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Inicio obtenerProductosDeTienda: {}", inicio);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoDTO> productos = tiendaService.obtenerProductosPorTienda(tiendaId, pageable);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("productos", productos.getContent());
        respuesta.put("currentPage", productos.getNumber());
        respuesta.put("totalItems", productos.getTotalElements());
        respuesta.put("totalPages", productos.getTotalPages());
        
        long fin = System.currentTimeMillis();
        logger.info("[ADMIN_TIENDAS] Fin obtenerProductosDeTienda: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(respuesta);
    }
}
