package com.cinepajeu.repository;

import com.cinepajeu.entity.VendaIngresso;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
