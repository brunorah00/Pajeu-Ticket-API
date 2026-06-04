package com.cinepajeu.repository;

import com.cinepajeu.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Page<Produto> findByAtivo(Boolean ativo, Pageable pageable);

    @Query("SELECT p FROM Produto p WHERE p.quantidadeEstoque < p.estoqueMinimo")
    List<Produto> findProdutosEstoqueBaixo();

    @Query("SELECT p FROM Produto p WHERE p.quantidadeEstoque < p.estoqueMinimo")
    Page<Produto> findProdutosEstoqueBaixo(Pageable pageable);

    List<Produto> findAllByOrderByCategoriaAscNomeAsc();
}
