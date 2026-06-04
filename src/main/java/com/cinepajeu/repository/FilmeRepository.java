package com.cinepajeu.repository;

import com.cinepajeu.entity.Filme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {
    Page<Filme> findByStatus(Boolean status, Pageable pageable);
}
