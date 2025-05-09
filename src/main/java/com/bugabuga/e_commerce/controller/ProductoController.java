package com.bugabuga.e_commerce.controller;

import com.bugabuga.e_commerce.model.dto.ApiResponse;
import com.bugabuga.e_commerce.model.dto.ProductoActualizacionDTO;
import com.bugabuga.e_commerce.model.dto.ProductoCreacionDTO;
import com.bugabuga.e_commerce.model.dto.ProductoDTO;
import com.bugabuga.e_commerce.service.ProductoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestión de productos
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Crea un nuevo producto
     * @param productoDTO datos del producto a crear
     * @return el producto creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('VENDEDOR')")
    public ResponseEntity<ApiResponse<ProductoDTO>> crearProducto(@Valid @RequestBody ProductoCreacionDTO productoDTO) {
        logger.info("Creando nuevo producto: {}", productoDTO.getNombre());
        ProductoDTO nuevoProducto = productoService.crearProducto(productoDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Producto creado con éxito", nuevoProducto));
    }

    /**
     * Obtiene un producto por su ID
     * @param id ID del producto
     * @return el producto encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> obtenerProducto(@PathVariable Long id) {
        logger.info("Obteniendo producto con ID: {}", id);
        ProductoDTO producto = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Producto obtenido con éxito", producto));
    }

    /**
     * Actualiza un producto existente
     * @param id ID del producto a actualizar
     * @param productoDTO datos actualizados del producto
     * @return el producto actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VENDEDOR')")
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoActualizacionDTO productoDTO) {
        
        logger.info("Actualizando producto con ID: {}", id);
        ProductoDTO productoActualizado = productoService.actualizarProducto(id, productoDTO);
        return ResponseEntity.ok(ApiResponse.success("Producto actualizado con éxito", productoActualizado));
    }

    /**
     * Elimina un producto (marcándolo como inactivo)
     * @param id ID del producto a eliminar
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VENDEDOR')")
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(@PathVariable Long id) {
        logger.info("Eliminando producto con ID: {}", id);
        productoService.eliminarProducto(id);
        return ResponseEntity.ok(ApiResponse.success("Producto eliminado con éxito"));
    }
}
