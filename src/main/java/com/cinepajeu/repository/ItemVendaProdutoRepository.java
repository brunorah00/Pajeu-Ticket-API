package com.cinepajeu.repository;

import com.cinepajeu.entity.ItemVendaProduto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemVendaProdutoRepository extends JpaRepository<ItemVendaProduto, Long> {

    @Query("SELECT SUM(i.quantidade) FROM ItemVendaProduto i WHERE i.vendaProduto.dataVenda BETWEEN :start AND :end")
    Long sumQuantidadeByDataVendaBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT i.produto.id, i.produto.nome, SUM(i.quantidade) as totalSold " +
           "FROM ItemVendaProduto i " +
           "GROUP BY i.produto.id, i.produto.nome " +
           "ORDER BY totalSold DESC")
    List<Object[]> findProdutosMaisVendidos(Pageable pageable);

    @Query("SELECT i FROM ItemVendaProduto i WHERE i.vendaProduto.dataVenda BETWEEN :start AND :end")
    List<ItemVendaProduto> findByDataVendaBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
