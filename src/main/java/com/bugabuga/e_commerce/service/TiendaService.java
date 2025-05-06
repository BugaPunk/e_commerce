package com.bugabuga.e_commerce.service;

import com.bugabuga.e_commerce.exception.ResourceNotFoundException;
import com.bugabuga.e_commerce.model.dto.ProductoDTO;
import com.bugabuga.e_commerce.model.entity.Categoria;
import com.bugabuga.e_commerce.model.entity.Producto;
import com.bugabuga.e_commerce.model.entity.Tienda;
import com.bugabuga.e_commerce.model.entity.Usuario;
import com.bugabuga.e_commerce.repository.CategoriaRepository;
import com.bugabuga.e_commerce.repository.ProductoRepository;
import com.bugabuga.e_commerce.repository.ReseñaRepository;
import com.bugabuga.e_commerce.repository.TiendaRepository;
import com.bugabuga.e_commerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TiendaService {

    @Autowired
    private TiendaRepository tiendaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ReseñaRepository reseñaRepository;

    @Transactional(readOnly = true)
    public Page<Tienda> obtenerTodasLasTiendas(Pageable pageable) {
        return tiendaRepository.findByActivaTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Tienda obtenerTiendaPorId(Long id) {
        return tiendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Tienda> obtenerTiendasPorPropietario(Long propietarioId) {
        // Verificar que el usuario existe
        usuarioRepository.findById(propietarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + propietarioId));
        
        return tiendaRepository.findByPropietarioId(propietarioId);
    }

    @Transactional
    public Tienda crearTienda(Tienda tienda) {
        // Verificar que el propietario existe
        Usuario propietario = usuarioRepository.findById(tienda.getPropietario().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + tienda.getPropietario().getId()));
        
        // Verificar que el nombre de la tienda no esté en uso
        if (tiendaRepository.existsByNombre(tienda.getNombre())) {
            throw new IllegalArgumentException("El nombre de la tienda ya está en uso");
        }
        
        // Establecer el propietario y activar la tienda
        tienda.setPropietario(propietario);
        tienda.setActiva(true);
        
        // Guardar la tienda
        return tiendaRepository.save(tienda);
    }

    @Transactional
    public Tienda actualizarTienda(Long id, Tienda tiendaActualizada) {
        // Verificar que la tienda existe
        Tienda tienda = tiendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con id: " + id));
        
        // Verificar que el nombre no está en uso por otra tienda
        if (!tienda.getNombre().equals(tiendaActualizada.getNombre()) && 
                tiendaRepository.existsByNombre(tiendaActualizada.getNombre())) {
            throw new IllegalArgumentException("El nombre de la tienda ya está en uso");
        }
        
        // Actualizar los campos de la tienda
        tienda.setNombre(tiendaActualizada.getNombre());
        tienda.setDescripcion(tiendaActualizada.getDescripcion());
        tienda.setLogoUrl(tiendaActualizada.getLogoUrl());
        
        // Guardar la tienda actualizada
        return tiendaRepository.save(tienda);
    }

    @Transactional
    public void desactivarTienda(Long id) {
        // Verificar que la tienda existe
        Tienda tienda = tiendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con id: " + id));
        
        // Desactivar la tienda
        tienda.setActiva(false);
        
        // Guardar la tienda actualizada
        tiendaRepository.save(tienda);
    }

    @Transactional
    public ProductoDTO agregarProductoATienda(Long tiendaId, ProductoDTO productoDTO) {
        // Verificar que la tienda existe
        Tienda tienda = tiendaRepository.findById(tiendaId)
                .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con id: " + tiendaId));
        
        // Verificar que la tienda está activa
        if (!tienda.isActiva()) {
            throw new IllegalArgumentException("No se pueden agregar productos a una tienda inactiva");
        }
        
        // Crear un nuevo producto
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setImagenUrl(productoDTO.getImagenUrl());
        producto.setTienda(tienda);
        producto.setActivo(true);
        
        // Asignar categoría si se proporciona
        if (productoDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + productoDTO.getCategoriaId()));
            producto.setCategoria(categoria);
        }
        
        // Guardar el producto
        Producto productoGuardado = productoRepository.save(producto);
        
        // Convertir el producto guardado a DTO y devolverlo
        return convertirADTO(productoGuardado);
    }

    @Transactional(readOnly = true)
    public Page<ProductoDTO> obtenerProductosPorTienda(Long tiendaId, Pageable pageable) {
        // Verificar que la tienda existe
        tiendaRepository.findById(tiendaId)
                .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con id: " + tiendaId));
        
        // Obtener los productos de la tienda
        Page<Producto> productos = productoRepository.findByTiendaIdAndActivoTrue(tiendaId, pageable);
        
        // Convertir los productos a DTOs
        return productos.map(this::convertirADTO);
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
