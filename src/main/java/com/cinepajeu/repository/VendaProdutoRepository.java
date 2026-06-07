package com.cinepajeu.repository;

import com.cinepajeu.entity.StatusPedidoBomboniere;
import com.cinepajeu.entity.VendaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendaProdutoRepository extends JpaRepository<VendaProduto, Long> {

    boolean existsByCodigoPedido(String codigoPedido);

    Optional<VendaProduto> findByCodigoPedido(String codigoPedido);

    @Query("SELECT SUM(v.valorTotal) FROM VendaProduto v WHERE v.dataVenda BETWEEN :start AND :end")
    BigDecimal sumValorTotalByDataVendaBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<VendaProduto> findByDataVendaBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT DISTINCT v FROM VendaProduto v
            LEFT JOIN FETCH v.itens i
            LEFT JOIN FETCH i.produto
            LEFT JOIN FETCH v.cliente
            WHERE v.dataVenda BETWEEN :start AND :end
            AND (:status IS NULL OR v.status = :status)
            ORDER BY v.dataVenda DESC
            """)
    List<VendaProduto> findPedidosComItens(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") StatusPedidoBomboniere status);

    @Query("""
            SELECT DISTINCT v FROM VendaProduto v
            LEFT JOIN FETCH v.itens i
            LEFT JOIN FETCH i.produto
            LEFT JOIN FETCH v.cliente
            WHERE v.id = :id
            """)
    Optional<VendaProduto> findByIdComItens(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT v FROM VendaProduto v
            LEFT JOIN FETCH v.itens i
            LEFT JOIN FETCH i.produto
            WHERE v.cliente.id = :clienteId
            AND v.dataVenda BETWEEN :start AND :end
            ORDER BY v.dataVenda DESC
            """)
    List<VendaProduto> findPedidosByCliente(
            @Param("clienteId") Long clienteId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
