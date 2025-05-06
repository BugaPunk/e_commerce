package com.bugabuga.e_commerce.repository;

import com.bugabuga.e_commerce.model.entity.Tienda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Long> {

    Page<Tienda> findByActivaTrue(Pageable pageable);

    Optional<Tienda> findByNombre(String nombre);

    List<Tienda> findByPropietarioId(Long propietarioId);

    boolean existsByNombre(String nombre);
}
