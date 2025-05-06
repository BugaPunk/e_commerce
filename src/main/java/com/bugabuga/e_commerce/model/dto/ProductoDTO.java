package com.bugabuga.e_commerce.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String imagenUrl;
    private Long tiendaId;
    private String tiendaNombre;
    private Long categoriaId;
    private String categoriaNombre;
    private Double calificacionPromedio;
    private Integer cantidadReseñas;
}
