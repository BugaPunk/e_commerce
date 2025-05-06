package com.bugabuga.e_commerce.service;

import com.bugabuga.e_commerce.exception.ResourceNotFoundException;
import com.bugabuga.e_commerce.model.dto.CarritoDTO;
import com.bugabuga.e_commerce.model.entity.Carrito;
import com.bugabuga.e_commerce.model.entity.ItemCarrito;
import com.bugabuga.e_commerce.model.entity.Producto;
import com.bugabuga.e_commerce.model.entity.Usuario;
import com.bugabuga.e_commerce.repository.CarritoRepository;
import com.bugabuga.e_commerce.repository.ProductoRepository;
import com.bugabuga.e_commerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public CarritoDTO obtenerCarritoPorUsuarioId(Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado para el usuario con id: " + usuarioId));
        
        return convertirADTO(carrito);
    }

    @Transactional
    public CarritoDTO agregarProductoAlCarrito(Long usuarioId, Long productoId, Integer cantidad) {
        // Obtener el carrito del usuario o crear uno nuevo si no existe
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Usuario usuario = usuarioRepository.findById(usuarioId)
                            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
                    
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    nuevoCarrito.setTotal(BigDecimal.ZERO);
                    return carritoRepository.save(nuevoCarrito);
                });
        
        // Obtener el producto
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));
        
        // Verificar si el producto ya está en el carrito
        Optional<ItemCarrito> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst();
        
        if (itemExistente.isPresent()) {
            // Actualizar la cantidad si el producto ya está en el carrito
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            // Agregar un nuevo item al carrito
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);
            carrito.getItems().add(nuevoItem);
        }
        
        // Recalcular el total del carrito
        carrito.calcularTotal();
        
        // Guardar el carrito actualizado
        Carrito carritoActualizado = carritoRepository.save(carrito);
        
        return convertirADTO(carritoActualizado);
    }

    @Transactional
    public CarritoDTO actualizarCantidadProducto(Long usuarioId, Long productoId, Integer cantidad) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado para el usuario con id: " + usuarioId));
        
        // Buscar el item en el carrito
        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado en el carrito"));
        
        // Actualizar la cantidad
        item.setCantidad(cantidad);
        
        // Recalcular el total del carrito
        carrito.calcularTotal();
        
        // Guardar el carrito actualizado
        Carrito carritoActualizado = carritoRepository.save(carrito);
        
        return convertirADTO(carritoActualizado);
    }

    @Transactional
    public CarritoDTO eliminarProductoDelCarrito(Long usuarioId, Long productoId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado para el usuario con id: " + usuarioId));
        
        // Eliminar el item del carrito
        carrito.getItems().removeIf(item -> item.getProducto().getId().equals(productoId));
        
        // Recalcular el total del carrito
        carrito.calcularTotal();
        
        // Guardar el carrito actualizado
        Carrito carritoActualizado = carritoRepository.save(carrito);
        
        return convertirADTO(carritoActualizado);
    }

    @Transactional
    public CarritoDTO vaciarCarrito(Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado para el usuario con id: " + usuarioId));
        
        // Vaciar el carrito
        carrito.getItems().clear();
        carrito.setTotal(BigDecimal.ZERO);
        
        // Guardar el carrito actualizado
        Carrito carritoActualizado = carritoRepository.save(carrito);
        
        return convertirADTO(carritoActualizado);
    }

    private CarritoDTO convertirADTO(Carrito carrito) {
        CarritoDTO carritoDTO = new CarritoDTO();
        carritoDTO.setId(carrito.getId());
        carritoDTO.setUsuarioId(carrito.getUsuario().getId());
        carritoDTO.setTotal(carrito.getTotal());
        
        List<CarritoDTO.ItemCarritoDTO> itemsDTO = new ArrayList<>();
        for (ItemCarrito item : carrito.getItems()) {
            CarritoDTO.ItemCarritoDTO itemDTO = new CarritoDTO.ItemCarritoDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProductoId(item.getProducto().getId());
            itemDTO.setProductoNombre(item.getProducto().getNombre());
            itemDTO.setProductoImagen(item.getProducto().getImagenUrl());
            itemDTO.setPrecioUnitario(item.getProducto().getPrecio());
            itemDTO.setCantidad(item.getCantidad());
            itemDTO.setSubtotal(item.getProducto().getPrecio().multiply(new BigDecimal(item.getCantidad())));
            
            itemsDTO.add(itemDTO);
        }
        
        carritoDTO.setItems(itemsDTO);
        
        return carritoDTO;
    }
}
