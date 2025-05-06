package com.bugabuga.e_commerce.service;

import com.bugabuga.e_commerce.exception.ResourceNotFoundException;
import com.bugabuga.e_commerce.model.dto.PedidoDTO;
import com.bugabuga.e_commerce.model.entity.Pago;
import com.bugabuga.e_commerce.model.entity.Pedido;
import com.bugabuga.e_commerce.model.entity.Usuario;
import com.bugabuga.e_commerce.repository.PagoRepository;
import com.bugabuga.e_commerce.repository.PedidoRepository;
import com.bugabuga.e_commerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public PedidoDTO procesarPago(Long usuarioId, Long pedidoId, Pago.MetodoPago metodoPago, Map<String, String> datosPago) {
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
        
        // Verificar que el pedido existe y pertenece al usuario
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + pedidoId));
        
        if (!pedido.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("El pedido no pertenece al usuario especificado");
        }
        
        // Verificar que el pedido está en estado PENDIENTE
        if (pedido.getEstado() != Pedido.EstadoPedido.PENDIENTE) {
            throw new IllegalArgumentException("El pedido ya ha sido procesado o cancelado");
        }
        
        // Crear un nuevo pago
        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMonto(pedido.getTotal());
        pago.setMetodoPago(metodoPago);
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado(Pago.EstadoPago.COMPLETADO);
        
        // Procesar los datos de pago según el método
        String referenciaPago = procesarDatosPago(metodoPago, datosPago);
        pago.setReferenciaPago(referenciaPago);
        
        // Guardar el pago
        pagoRepository.save(pago);
        
        // Actualizar el estado del pedido
        pedido.setEstado(Pedido.EstadoPedido.PAGADO);
        pedido.setPago(pago);
        pedidoRepository.save(pedido);
        
        // Convertir el pedido a DTO y devolverlo
        return convertirPedidoADTO(pedido);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerInformacionPago(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + pagoId));
        
        Map<String, Object> infoPago = new HashMap<>();
        infoPago.put("id", pago.getId());
        infoPago.put("monto", pago.getMonto());
        infoPago.put("metodoPago", pago.getMetodoPago().toString());
        infoPago.put("fechaPago", pago.getFechaPago());
        infoPago.put("referenciaPago", pago.getReferenciaPago());
        infoPago.put("estado", pago.getEstado().toString());
        infoPago.put("pedidoId", pago.getPedido().getId());
        
        return infoPago;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerHistorialPagosUsuario(Long usuarioId) {
        // Verificar que el usuario existe
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
        
        // Obtener todos los pedidos del usuario que tengan pagos
        List<Pedido> pedidos = pedidoRepository.findByUsuarioIdAndEstadoIn(
                usuarioId, 
                List.of(Pedido.EstadoPedido.PAGADO, Pedido.EstadoPedido.EN_PROCESO, Pedido.EstadoPedido.ENVIADO, Pedido.EstadoPedido.ENTREGADO)
        );
        
        // Extraer los pagos de los pedidos
        List<Map<String, Object>> pagos = pedidos.stream()
                .filter(pedido -> pedido.getPago() != null)
                .map(pedido -> {
                    Pago pago = pedido.getPago();
                    Map<String, Object> infoPago = new HashMap<>();
                    infoPago.put("id", pago.getId());
                    infoPago.put("monto", pago.getMonto());
                    infoPago.put("metodoPago", pago.getMetodoPago().toString());
                    infoPago.put("fechaPago", pago.getFechaPago());
                    infoPago.put("referenciaPago", pago.getReferenciaPago());
                    infoPago.put("estado", pago.getEstado().toString());
                    infoPago.put("pedidoId", pedido.getId());
                    infoPago.put("estadoPedido", pedido.getEstado().toString());
                    return infoPago;
                })
                .collect(Collectors.toList());
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("pagos", pagos);
        resultado.put("totalPagos", pagos.size());
        
        return resultado;
    }

    @Transactional
    public Map<String, Object> procesarReembolso(Long pagoId, Map<String, String> datosReembolso) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + pagoId));
        
        // Verificar que el pago está en estado COMPLETADO
        if (pago.getEstado() != Pago.EstadoPago.COMPLETADO) {
            throw new IllegalArgumentException("El pago no está en estado COMPLETADO");
        }
        
        // Procesar el reembolso
        String motivoReembolso = datosReembolso.getOrDefault("motivo", "Reembolso solicitado por el cliente");
        
        // Actualizar el estado del pago
        pago.setEstado(Pago.EstadoPago.REEMBOLSADO);
        pagoRepository.save(pago);
        
        // Actualizar el estado del pedido
        Pedido pedido = pago.getPedido();
        pedido.setEstado(Pedido.EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);
        
        // Preparar la respuesta
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("pagoId", pago.getId());
        resultado.put("estado", pago.getEstado().toString());
        resultado.put("pedidoId", pedido.getId());
        resultado.put("estadoPedido", pedido.getEstado().toString());
        resultado.put("mensaje", "Reembolso procesado correctamente: " + motivoReembolso);
        
        return resultado;
    }

    private String procesarDatosPago(Pago.MetodoPago metodoPago, Map<String, String> datosPago) {
        // Aquí se implementaría la lógica para procesar el pago con un gateway de pagos real
        // Por ahora, simplemente generamos una referencia de pago ficticia
        
        String referencia = "REF-" + System.currentTimeMillis();
        
        switch (metodoPago) {
            case TARJETA_CREDITO:
            case TARJETA_DEBITO:
                // Validar datos de tarjeta
                if (!datosPago.containsKey("numeroTarjeta") || !datosPago.containsKey("cvv")) {
                    throw new IllegalArgumentException("Datos de tarjeta incompletos");
                }
                referencia = "CARD-" + referencia;
                break;
            case TRANSFERENCIA:
                referencia = "TRANS-" + referencia;
                break;
            case PAYPAL:
                referencia = "PP-" + referencia;
                break;
            case EFECTIVO:
                referencia = "CASH-" + referencia;
                break;
            default:
                throw new IllegalArgumentException("Método de pago no soportado");
        }
        
        return referencia;
    }

    private PedidoDTO convertirPedidoADTO(Pedido pedido) {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(pedido.getId());
        pedidoDTO.setUsuarioId(pedido.getUsuario().getId());
        pedidoDTO.setUsuarioNombre(pedido.getUsuario().getNombre() + " " + pedido.getUsuario().getApellido());
        pedidoDTO.setTotal(pedido.getTotal());
        pedidoDTO.setFechaCreacion(pedido.getFechaCreacion());
        pedidoDTO.setEstado(pedido.getEstado());
        pedidoDTO.setDireccionEnvio(pedido.getDireccionEnvio());
        pedidoDTO.setTelefonoContacto(pedido.getTelefonoContacto());
        
        // Convertir items del pedido
        List<PedidoDTO.ItemPedidoDTO> itemsDTO = new ArrayList<>();
        pedido.getItems().forEach(item -> {
            PedidoDTO.ItemPedidoDTO itemDTO = new PedidoDTO.ItemPedidoDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProductoId(item.getProducto().getId());
            itemDTO.setProductoNombre(item.getProducto().getNombre());
            itemDTO.setProductoImagen(item.getProducto().getImagenUrl());
            itemDTO.setCantidad(item.getCantidad());
            itemDTO.setPrecioUnitario(item.getPrecioUnitario());
            itemDTO.setSubtotal(item.getSubtotal());
            
            itemsDTO.add(itemDTO);
        });
        pedidoDTO.setItems(itemsDTO);
        
        // Convertir información de pago si existe
        if (pedido.getPago() != null) {
            Pago pago = pedido.getPago();
            PedidoDTO.PagoDTO pagoDTO = new PedidoDTO.PagoDTO();
            pagoDTO.setId(pago.getId());
            pagoDTO.setMonto(pago.getMonto());
            pagoDTO.setMetodoPago(pago.getMetodoPago().toString());
            pagoDTO.setFechaPago(pago.getFechaPago());
            pagoDTO.setReferenciaPago(pago.getReferenciaPago());
            pagoDTO.setEstado(pago.getEstado().toString());
            
            pedidoDTO.setPago(pagoDTO);
        }
        
        return pedidoDTO;
    }
}
