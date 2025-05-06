package com.bugabuga.e_commerce.repository;

import com.bugabuga.e_commerce.model.entity.Reseña;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReseñaRepository extends JpaRepository<Reseña, Long> {

    Page<Reseña> findByProductoId(Long productoId, Pageable pageable);

    Page<Reseña> findByUsuarioId(Long usuarioId, Pageable pageable);

    Optional<Reseña> findByUsuarioIdAndProductoId(Long usuarioId, Long productoId);

    @Query("SELECT AVG(r.calificacion) FROM Reseña r WHERE r.producto.id = :productoId")
    Double calcularCalificacionPromedio(@Param("productoId") Long productoId);

    @Query("SELECT COUNT(r) FROM Reseña r WHERE r.producto.id = :productoId")
    Integer contarReseñasPorProducto(@Param("productoId") Long productoId);
}
