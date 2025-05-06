package com.bugabuga.e_commerce.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDTO {
    private Long id;
    private Long usuarioId;
    private List<ItemCarritoDTO> items = new ArrayList<>();
    private BigDecimal total;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemCarritoDTO {
        private Long id;
        private Long productoId;
        private String productoNombre;
        private String productoImagen;
        private BigDecimal precioUnitario;
        private Integer cantidad;
        private BigDecimal subtotal;
    }
}
