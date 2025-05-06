package com.bugabuga.e_commerce.model.dto;

import com.bugabuga.e_commerce.model.entity.Pedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private List<ItemPedidoDTO> items = new ArrayList<>();
    private BigDecimal total;
    private LocalDateTime fechaCreacion;
    private Pedido.EstadoPedido estado;
    private String direccionEnvio;
    private String telefonoContacto;
    private PagoDTO pago;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedidoDTO {
        private Long id;
        private Long productoId;
        private String productoNombre;
        private String productoImagen;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagoDTO {
        private Long id;
        private BigDecimal monto;
        private String metodoPago;
        private LocalDateTime fechaPago;
        private String referenciaPago;
        private String estado;
    }
}
