package com.bugabuga.e_commerce.controller;

import com.bugabuga.e_commerce.model.dto.ApiResponse;
import com.bugabuga.e_commerce.model.dto.ProductoDTO;
import com.bugabuga.e_commerce.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/productos")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<ProductoDTO> pageProductos = productoService.obtenerTodosLosProductos(pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("productos", pageProductos.getContent());
        data.put("currentPage", pageProductos.getNumber());
        data.put("totalItems", pageProductos.getTotalElements());
        data.put("totalPages", pageProductos.getTotalPages());

        return ResponseEntity.ok(ApiResponse.success("Productos obtenidos con éxito", data));
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> obtenerProductoPorId(@PathVariable Long id) {
        ProductoDTO producto = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Producto obtenido con éxito", producto));
    }

    @GetMapping("/productos/buscar")
    public ResponseEntity<ApiResponse<Map<String, Object>>> buscarProductos(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoDTO> pageProductos = productoService.buscarProductos(keyword, pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("productos", pageProductos.getContent());
        data.put("currentPage", pageProductos.getNumber());
        data.put("totalItems", pageProductos.getTotalElements());
        data.put("totalPages", pageProductos.getTotalPages());

        return ResponseEntity.ok(ApiResponse.success("Búsqueda realizada con éxito", data));
    }

    @GetMapping("/tiendas/{tiendaId}/productos")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerProductosPorTienda(
            @PathVariable Long tiendaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoDTO> pageProductos = productoService.obtenerProductosPorTienda(tiendaId, pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("productos", pageProductos.getContent());
        data.put("currentPage", pageProductos.getNumber());
        data.put("totalItems", pageProductos.getTotalElements());
        data.put("totalPages", pageProductos.getTotalPages());

        return ResponseEntity.ok(ApiResponse.success("Productos de la tienda obtenidos con éxito", data));
    }

    @GetMapping("/categorias/{categoriaId}/productos")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerProductosPorCategoria(
            @PathVariable Long categoriaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoDTO> pageProductos = productoService.obtenerProductosPorCategoria(categoriaId, pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("productos", pageProductos.getContent());
        data.put("currentPage", pageProductos.getNumber());
        data.put("totalItems", pageProductos.getTotalElements());
        data.put("totalPages", pageProductos.getTotalPages());

        return ResponseEntity.ok(ApiResponse.success("Productos de la categoría obtenidos con éxito", data));
    }

    @GetMapping("/productos/recientes")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> obtenerProductosRecientes() {
        List<ProductoDTO> productos = productoService.obtenerProductosRecientes();
        return ResponseEntity.ok(ApiResponse.success("Productos recientes obtenidos con éxito", productos));
    }
}
