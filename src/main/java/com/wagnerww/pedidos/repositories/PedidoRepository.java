package com.wagnerww.pedidos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wagnerww.pedidos.domain.Categoria;
import com.wagnerww.pedidos.domain.Pagamento;
import com.wagnerww.pedidos.domain.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

}
