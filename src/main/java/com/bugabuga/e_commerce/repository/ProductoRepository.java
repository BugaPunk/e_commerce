package com.bugabuga.e_commerce.repository;

import com.bugabuga.e_commerce.model.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Page<Producto> findByActivoTrue(Pageable pageable);

    Page<Producto> findByTiendaIdAndActivoTrue(Long tiendaId, Pageable pageable);

    Page<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId, Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Producto> buscarProductos(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.tienda.id = :tiendaId AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Producto> buscarProductosPorTienda(@Param("tiendaId") Long tiendaId, 
                                           @Param("keyword") String keyword, 
                                           Pageable pageable);

    List<Producto> findTop10ByActivoTrueOrderByIdDesc();
}
