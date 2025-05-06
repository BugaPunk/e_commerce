package com.bugabuga.e_commerce.repository;

import com.bugabuga.e_commerce.model.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByPedidoUsuarioId(Long usuarioId);

    List<Pago> findByPedidoId(Long pedidoId);
}
