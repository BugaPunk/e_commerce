package com.bugabuga.e_commerce.service;

import com.bugabuga.e_commerce.model.dto.ProductoActualizacionDTO;
import com.bugabuga.e_commerce.model.dto.ProductoCreacionDTO;
import com.bugabuga.e_commerce.model.dto.ProductoDTO;
import com.bugabuga.e_commerce.model.dto.UsuarioDTO;
import com.bugabuga.e_commerce.model.dto.UsuarioRegistroDTO;
import com.bugabuga.e_commerce.model.entity.Categoria;
import com.bugabuga.e_commerce.model.entity.Producto;
import com.bugabuga.e_commerce.model.entity.Rol;
import com.bugabuga.e_commerce.model.entity.Tienda;
import com.bugabuga.e_commerce.model.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio centralizado para el mapeo entre entidades y DTOs
 */
@Service
public class MappingService {
    
    /**
     * Convierte un Usuario a UsuarioDTO
     */
    public UsuarioDTO mapToUsuarioDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        
        // No incluir la contraseña en el DTO por seguridad
        
        // Mapear roles
        if (usuario.getRoles() != null) {
            Set<String> roles = usuario.getRoles().stream()
                    .map(Rol::getNombre)
                    .collect(Collectors.toSet());
            dto.setRoles(roles);
        }
        
        return dto;
    }
    
    /**
     * Convierte un UsuarioRegistroDTO a Usuario
     */
    public Usuario mapToUsuario(UsuarioRegistroDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setPassword(dto.getPassword());
        usuario.setActivo(true);
        
        return usuario;
    }
    
    /**
     * Convierte un Producto a ProductoDTO
     */
    public ProductoDTO mapToProductoDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setImagenUrl(producto.getImagenUrl());
        
        // Mapear tienda
        Tienda tienda = producto.getTienda();
        if (tienda != null) {
            dto.setTiendaId(tienda.getId());
            dto.setTiendaNombre(tienda.getNombre());
        }
        
        // Mapear categoría
        Categoria categoria = producto.getCategoria();
        if (categoria != null) {
            dto.setCategoriaId(categoria.getId());
            dto.setCategoriaNombre(categoria.getNombre());
        }
        
        // Calcular calificación promedio y cantidad de reseñas
        if (producto.getReseñas() != null && !producto.getReseñas().isEmpty()) {
            double promedio = producto.getReseñas().stream()
                    .mapToInt(r -> r.getCalificacion())
                    .average()
                    .orElse(0.0);
            
            dto.setCalificacionPromedio(promedio);
            dto.setCantidadReseñas(producto.getReseñas().size());
        } else {
            dto.setCalificacionPromedio(0.0);
            dto.setCantidadReseñas(0);
        }
        
        return dto;
    }
    
    /**
     * Convierte una lista de Productos a lista de ProductoDTO
     */
    public List<ProductoDTO> mapToProductoDTOList(List<Producto> productos) {
        if (productos == null) {
            return List.of();
        }
        
        return productos.stream()
                .map(this::mapToProductoDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convierte un ProductoCreacionDTO a Producto
     */
    public Producto mapToProducto(ProductoCreacionDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setImagenUrl(dto.getImagenUrl());
        
        // Tienda y Categoría se establecen en el servicio
        
        return producto;
    }
    
    /**
     * Actualiza un Producto existente con datos de ProductoActualizacionDTO
     */
    public void updateProductoFromDTO(Producto producto, ProductoActualizacionDTO dto) {
        if (producto == null || dto == null) {
            return;
        }
        
        if (dto.getNombre() != null) {
            producto.setNombre(dto.getNombre());
        }
        
        if (dto.getDescripcion() != null) {
            producto.setDescripcion(dto.getDescripcion());
        }
        
        if (dto.getPrecio() != null) {
            producto.setPrecio(dto.getPrecio());
        }
        
        if (dto.getStock() != null) {
            producto.setStock(dto.getStock());
        }
        
        if (dto.getImagenUrl() != null) {
            producto.setImagenUrl(dto.getImagenUrl());
        }
        
        // Categoría se actualiza en el servicio
    }
}
