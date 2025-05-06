package com.bugabuga.e_commerce.controller;

import com.bugabuga.e_commerce.model.dto.PedidoDTO;
import com.bugabuga.e_commerce.model.entity.Pago;
import com.bugabuga.e_commerce.service.PagoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@Validated
public class PagoController {

    private final PagoService pagoService;
    private static final Logger logger = LoggerFactory.getLogger(PagoController.class);

    @Autowired
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping("/procesar")
    @Transactional
    public ResponseEntity<Map<String, Object>> procesarPago(
            @RequestParam Long usuarioId,
            @RequestParam Long pedidoId,
            @RequestParam Pago.MetodoPago metodoPago,
            @RequestBody Map<String, String> datosPago) {
        long inicio = System.currentTimeMillis();
        logger.info("[PAGO] Inicio procesarPago: {}", inicio);
        
        PedidoDTO pedidoPagado = pagoService.procesarPago(usuarioId, pedidoId, metodoPago, datosPago);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("pedido", pedidoPagado);
        respuesta.put("mensaje", "Pago procesado correctamente");
        respuesta.put("estado", "COMPLETADO");
        
        long fin = System.currentTimeMillis();
        logger.info("[PAGO] Fin procesarPago: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }

    @GetMapping("/{pagoId}")
    public ResponseEntity<Map<String, Object>> obtenerInformacionPago(@PathVariable Long pagoId) {
        long inicio = System.currentTimeMillis();
        logger.info("[PAGO] Inicio obtenerInformacionPago: {}", inicio);
        
        Map<String, Object> infoPago = pagoService.obtenerInformacionPago(pagoId);
        
        long fin = System.currentTimeMillis();
        logger.info("[PAGO] Fin obtenerInformacionPago: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(infoPago);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerHistorialPagosUsuario(@PathVariable Long usuarioId) {
        long inicio = System.currentTimeMillis();
        logger.info("[PAGO] Inicio obtenerHistorialPagosUsuario: {}", inicio);
        
        Map<String, Object> historialPagos = pagoService.obtenerHistorialPagosUsuario(usuarioId);
        
        long fin = System.currentTimeMillis();
        logger.info("[PAGO] Fin obtenerHistorialPagosUsuario: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(historialPagos);
    }

    @PostMapping("/reembolso/{pagoId}")
    @Transactional
    public ResponseEntity<Map<String, Object>> procesarReembolso(
            @PathVariable Long pagoId,
            @RequestBody Map<String, String> datosReembolso) {
        long inicio = System.currentTimeMillis();
        logger.info("[PAGO] Inicio procesarReembolso: {}", inicio);
        
        Map<String, Object> resultadoReembolso = pagoService.procesarReembolso(pagoId, datosReembolso);
        
        long fin = System.currentTimeMillis();
        logger.info("[PAGO] Fin procesarReembolso: {} (Duración: {} ms)", fin, (fin-inicio));
        
        return ResponseEntity.ok(resultadoReembolso);
    }
}
