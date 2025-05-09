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

    @Query(value = "SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%')))",
           countQuery = "SELECT COUNT(p) FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Producto> buscarProductos(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT p FROM Producto p WHERE p.activo = true AND p.tienda.id = :tiendaId AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%')))",
           countQuery = "SELECT COUNT(p) FROM Producto p WHERE p.activo = true AND p.tienda.id = :tiendaId AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Producto> buscarProductosPorTienda(@Param("tiendaId") Long tiendaId,
                                           @Param("keyword") String keyword,
                                           Pageable pageable);

    List<Producto> findTop10ByActivoTrueOrderByIdDesc();

    @Query(value = "SELECT p FROM Producto p JOIN FETCH p.tienda JOIN FETCH p.categoria WHERE p.id = :id")
    Producto findByIdWithTiendaAndCategoria(@Param("id") Long id);

    @Query(value = "SELECT p FROM Producto p JOIN FETCH p.tienda WHERE p.activo = true ORDER BY p.id DESC",
           countQuery = "SELECT COUNT(p) FROM Producto p WHERE p.activo = true")
    Page<Producto> findAllActiveWithTienda(Pageable pageable);

    @Query("SELECT COUNT(p) FROM Producto p WHERE p.tienda.id = :tiendaId AND p.activo = true")
    Long countByTiendaIdAndActivoTrue(@Param("tiendaId") Long tiendaId);
}
