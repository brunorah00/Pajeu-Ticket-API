package com.cinepajeu.repository;

import com.cinepajeu.entity.VendaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaProdutoRepository extends JpaRepository<VendaProduto, Long> {

    @Query("SELECT SUM(v.valorTotal) FROM VendaProduto v WHERE v.dataVenda BETWEEN :start AND :end")
    BigDecimal sumValorTotalByDataVendaBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<VendaProduto> findByDataVendaBetween(LocalDateTime start, LocalDateTime end);
}
