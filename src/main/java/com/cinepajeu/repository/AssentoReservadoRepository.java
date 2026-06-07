package com.cinepajeu.repository;

import com.cinepajeu.entity.AssentoReservado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AssentoReservadoRepository extends JpaRepository<AssentoReservado, Long> {

    List<AssentoReservado> findBySessaoId(Long sessaoId);

    boolean existsBySessaoIdAndCodigoAssentoIn(Long sessaoId, Set<String> codigos);

    List<AssentoReservado> findByVenda_IdOrderByCodigoAssentoAsc(Long vendaId);
}
