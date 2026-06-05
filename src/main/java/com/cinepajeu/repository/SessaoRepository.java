package com.cinepajeu.repository;

import com.cinepajeu.entity.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {

    List<Sessao> findByFilmeId(Long filmeId);

    @Query("SELECT s FROM Sessao s JOIN FETCH s.filme JOIN FETCH s.sala WHERE s.data = :data")
    List<Sessao> findByData(@Param("data") LocalDate data);
}
