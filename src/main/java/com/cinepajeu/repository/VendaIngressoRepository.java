package com.cinepajeu.repository;

import com.cinepajeu.entity.StatusPedidoBomboniere;
import com.cinepajeu.entity.VendaIngresso;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaIngressoRepository extends JpaRepository<VendaIngresso, Long> {

    @Query("SELECT SUM(v.valorTotal) FROM VendaIngresso v WHERE v.dataVenda BETWEEN :start AND :end")
    BigDecimal sumValorTotalByDataVendaBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT SUM(v.quantidade) FROM VendaIngresso v WHERE v.dataVenda BETWEEN :start AND :end")
    Long sumQuantidadeByDataVendaBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT v.sessao.id, v.sessao.filme.titulo, SUM(v.quantidade) as totalSold " +
           "FROM VendaIngresso v " +
           "GROUP BY v.sessao.id, v.sessao.filme.titulo " +
           "ORDER BY totalSold DESC")
    List<Object[]> findSessoesMaisVendidas(Pageable pageable);

    List<VendaIngresso> findByDataVendaBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(v) FROM VendaIngresso v WHERE v.sessao.filme.id = :filmeId")
    long countByFilmeId(@Param("filmeId") Long filmeId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM VendaIngresso v WHERE v.sessao.filme.id = :filmeId")
    void deleteByFilmeId(@Param("filmeId") Long filmeId);

    @Query("SELECT COUNT(v) FROM VendaIngresso v WHERE v.sessao.id = :sessaoId")
    long countBySessaoId(@Param("sessaoId") Long sessaoId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM VendaIngresso v WHERE v.sessao.id = :sessaoId")
    void deleteBySessaoId(@Param("sessaoId") Long sessaoId);

    boolean existsByCodigoPedido(String codigoPedido);

    @Query("""
            SELECT DISTINCT v FROM VendaIngresso v
            JOIN FETCH v.sessao s
            JOIN FETCH s.filme
            LEFT JOIN FETCH v.cliente
            WHERE v.dataVenda BETWEEN :start AND :end
            AND v.codigoPedido IS NOT NULL
            AND v.codigoPedido NOT LIKE 'ING-LEG%'
            AND (:status IS NULL OR v.status = :status)
            ORDER BY v.dataVenda DESC
            """)
    List<VendaIngresso> findPedidosDoDia(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") StatusPedidoBomboniere status);

    @Query("""
            SELECT DISTINCT v FROM VendaIngresso v
            JOIN FETCH v.sessao s
            JOIN FETCH s.filme
            LEFT JOIN FETCH v.cliente
            WHERE v.id = :id
            """)
    java.util.Optional<VendaIngresso> findPedidoById(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT v FROM VendaIngresso v
            JOIN FETCH v.sessao s
            JOIN FETCH s.filme
            WHERE v.cliente.id = :clienteId
            AND v.dataVenda BETWEEN :start AND :end
            AND v.codigoPedido IS NOT NULL
            AND v.codigoPedido NOT LIKE 'ING-LEG%'
            ORDER BY v.dataVenda DESC
            """)
    List<VendaIngresso> findPedidosByCliente(
            @Param("clienteId") Long clienteId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
