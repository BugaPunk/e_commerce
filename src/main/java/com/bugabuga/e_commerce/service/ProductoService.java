package com.bugabuga.e_commerce.service;

import com.bugabuga.e_commerce.model.dto.ProductoDTO;
import com.bugabuga.e_commerce.model.entity.Categoria;
import com.bugabuga.e_commerce.model.entity.Producto;
import com.bugabuga.e_commerce.model.entity.Tienda;
import com.bugabuga.e_commerce.exception.ResourceNotFoundException;
import com.bugabuga.e_commerce.repository.CategoriaRepository;
import com.bugabuga.e_commerce.repository.ProductoRepository;
import com.bugabuga.e_commerce.repository.ReseñaRepository;
import com.bugabuga.e_commerce.repository.TiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ReseñaRepository reseñaRepository;

    @Transactional(readOnly = true)
    public Page<ProductoDTO> obtenerTodosLosProductos(Pageable pageable) {
        Page<Producto> productos = productoRepository.findByActivoTrue(pageable);
        return productos.map(this::convertirADTO);
    }

    @Transactional(readOnly = true)
    public ProductoDTO obtenerProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        
        if (!producto.isActivo()) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        
        return convertirADTO(producto);
    }

    @Transactional
    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        Tienda tienda = tiendaRepository.findById(productoDTO.getTiendaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con id: " + productoDTO.getTiendaId()));
        
        Categoria categoria = null;
        if (productoDTO.getCategoriaId() != null) {
            categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + productoDTO.getCategoriaId()));
        }
        
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setImagenUrl(productoDTO.getImagenUrl());
        producto.setTienda(tienda);
        producto.setCategoria(categoria);
        producto.setActivo(true);
        
        Producto productoGuardado = productoRepository.save(producto);
        return convertirADTO(productoGuardado);
    }

    @Transactional
    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        
        Categoria categoria = null;
        if (productoDTO.getCategoriaId() != null) {
            categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + productoDTO.getCategoriaId()));
        }
        
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setImagenUrl(productoDTO.getImagenUrl());
        producto.setCategoria(categoria);
        
        Producto productoActualizado = productoRepository.save(producto);
        return convertirADTO(productoActualizado);
    }

    @Transactional
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Transactional(readOnly = true)
    public Page<ProductoDTO> buscarProductos(String keyword, Pageable pageable) {
        Page<Producto> productos = productoRepository.buscarProductos(keyword, pageable);
        return productos.map(this::convertirADTO);
    }

    @Transactional(readOnly = true)
    public Page<ProductoDTO> obtenerProductosPorTienda(Long tiendaId, Pageable pageable) {
        Page<Producto> productos = productoRepository.findByTiendaIdAndActivoTrue(tiendaId, pageable);
        return productos.map(this::convertirADTO);
    }

    @Transactional(readOnly = true)
    public Page<ProductoDTO> obtenerProductosPorCategoria(Long categoriaId, Pageable pageable) {
        Page<Producto> productos = productoRepository.findByCategoriaIdAndActivoTrue(categoriaId, pageable);
        return productos.map(this::convertirADTO);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerProductosRecientes() {
        List<Producto> productos = productoRepository.findTop10ByActivoTrueOrderByIdDesc();
        return productos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setImagenUrl(producto.getImagenUrl());
        
        if (producto.getTienda() != null) {
            dto.setTiendaId(producto.getTienda().getId());
            dto.setTiendaNombre(producto.getTienda().getNombre());
        }
        
        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getId());
            dto.setCategoriaNombre(producto.getCategoria().getNombre());
        }
        
        // Obtener calificación promedio y cantidad de reseñas
        Double calificacionPromedio = reseñaRepository.calcularCalificacionPromedio(producto.getId());
        Integer cantidadReseñas = reseñaRepository.contarReseñasPorProducto(producto.getId());
        
        dto.setCalificacionPromedio(calificacionPromedio != null ? calificacionPromedio : 0.0);
        dto.setCantidadReseñas(cantidadReseñas != null ? cantidadReseñas : 0);
        
        return dto;
    }
}
