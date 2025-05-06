package com.bugabuga.e_commerce.service;

import com.bugabuga.e_commerce.exception.ResourceNotFoundException;
import com.bugabuga.e_commerce.model.entity.Producto;
import com.bugabuga.e_commerce.model.entity.Reseña;
import com.bugabuga.e_commerce.model.entity.Usuario;
import com.bugabuga.e_commerce.repository.ProductoRepository;
import com.bugabuga.e_commerce.repository.ReseñaRepository;
import com.bugabuga.e_commerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReseñaService {

    @Autowired
    private ReseñaRepository reseñaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public Page<Reseña> obtenerReseñasPorProducto(Long productoId, Pageable pageable) {
        // Verificar que el producto existe
        productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));
        
        return reseñaRepository.findByProductoId(productoId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Reseña> obtenerReseñasPorUsuario(Long usuarioId, Pageable pageable) {
        // Verificar que el usuario existe
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
        
        return reseñaRepository.findByUsuarioId(usuarioId, pageable);
    }

    @Transactional
    public Reseña crearReseña(Reseña reseña) {
        // Verificar que el producto existe
        Producto producto = productoRepository.findById(reseña.getProducto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + reseña.getProducto().getId()));
        
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findById(reseña.getUsuario().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + reseña.getUsuario().getId()));
        
        // Verificar si el usuario ya ha reseñado este producto
        reseñaRepository.findByUsuarioIdAndProductoId(usuario.getId(), producto.getId())
                .ifPresent(r -> {
                    throw new IllegalArgumentException("El usuario ya ha reseñado este producto");
                });
        
        // Validar la calificación (1-5 estrellas)
        if (reseña.getCalificacion() < 1 || reseña.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5 estrellas");
        }
        
        // Establecer la fecha de creación
        reseña.setFechaCreacion(LocalDateTime.now());
        
        // Guardar la reseña
        return reseñaRepository.save(reseña);
    }

    @Transactional
    public Reseña actualizarReseña(Long id, Reseña reseñaActualizada) {
        // Verificar que la reseña existe
        Reseña reseña = reseñaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada con id: " + id));
        
        // Validar la calificación (1-5 estrellas)
        if (reseñaActualizada.getCalificacion() < 1 || reseñaActualizada.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5 estrellas");
        }
        
        // Actualizar los campos de la reseña
        reseña.setCalificacion(reseñaActualizada.getCalificacion());
        reseña.setComentario(reseñaActualizada.getComentario());
        
        // Guardar la reseña actualizada
        return reseñaRepository.save(reseña);
    }

    @Transactional
    public void eliminarReseña(Long id) {
        // Verificar que la reseña existe
        Reseña reseña = reseñaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada con id: " + id));
        
        // Eliminar la reseña
        reseñaRepository.delete(reseña);
    }

    @Transactional(readOnly = true)
    public Double calcularCalificacionPromedio(Long productoId) {
        // Verificar que el producto existe
        productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));
        
        return reseñaRepository.calcularCalificacionPromedio(productoId);
    }
}
