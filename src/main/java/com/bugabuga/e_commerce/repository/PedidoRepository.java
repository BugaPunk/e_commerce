package com.bugabuga.e_commerce.repository;

import com.bugabuga.e_commerce.model.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Page<Pedido> findByUsuarioId(Long usuarioId, Pageable pageable);

    List<Pedido> findByUsuarioIdAndEstadoIn(Long usuarioId, List<Pedido.EstadoPedido> estados);
}
